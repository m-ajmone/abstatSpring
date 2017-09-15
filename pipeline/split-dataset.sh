#!/bin/bash

DatasetName=$1
tmpDatasetFile="data/DsAndOnt/dataset/$DatasetName/organized-splitted-deduplicated-tmp-file"
orgDatasetFile="data/DsAndOnt/dataset/$DatasetName/organized-splitted-deduplicated"
NProc=4
DatasetFile="data/DsAndOnt/dataset/$DatasetName/$DatasetName.nt"
dbgCmd=""
AwkScript="awk-scripts/organize_data.awk"

IFS=',' read -a splitters <<< "0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,%,_,others" #Allineare con quanto presente in organize:data, se modifico

#Funzioni utili alla gestione delle code di esecuzione di processi
function queue {
	QUEUE="$QUEUE $1"
	NUM=$(($NUM+1))
}

function regeneratequeue {
	OLDREQUEUE=$QUEUE
	QUEUE=""
	NUM=0
	for PID in $OLDREQUEUE
	do
		if [ -d /proc/$PID  ] ; then
			QUEUE="$QUEUE $PID"
			NUM=$(($NUM+1))
		fi
	done
}

function checkqueue {
	OLDCHQUEUE=$QUEUE
	for PID in $OLDCHQUEUE
	do
		if [ ! -d /proc/$PID ] ; then
			regeneratequeue # at least one PID has finished
			break
		fi
	done
}

echo "---Start: Organize and Split files---"

startBlock=$SECONDS

#Divido i file da organizzare in NProc parti uguali l'uno così da parallelizzare l'organizzazione
rm -rf $tmpDatasetFile #Rimuovo la directory che conterrà i file temporanei
mkdir -p $tmpDatasetFile #Creo la directory che conterrà i file temporanei

#Calcolo la dimensione dei file da splittare
dataSize1=$(stat --printf="%s" $DatasetFile) 

let "dataBlockSize1=($dataSize1/$NProc)+10000000" #Aggiungo 10000000 così da assicurarmi di salvare tutte le informazioni nel passo successivo

#Processo da eseguire per lo splittaggio
splitFile="split -u -C $dataBlockSize1 $DatasetFile $tmpDatasetFile/1_lod_part_" #-u per scrittura diretta senza bufferizzazione

#Splitto il file utilizzando dataBlockSize
eval ${dbgCmd}""${splitFile}
sync #Mi assicuro che tutte le informazioni siano scritte su file

#Creo le stringhe contenenti i file da organizzare
filePartCount=0
stringFile[0]="aa"
stringFile[1]="ab"
stringFile[2]="ac"
stringFile[3]="ad"
for (( i=0; i<${#stringFile[@]}; i++ ))
do
	filePart=""
	if [ -f $tmpDatasetFile/1_lod_part_${stringFile[$i]} ];
	then
		if [ filePart == "" ]
		then
			filePart="$tmpDatasetFile/1_lod_part_${stringFile[$i]}"
		else
			filePart="${filePart} $tmpDatasetFile/1_lod_part_${stringFile[$i]}"
		fi
	fi
	filePartCom[$filePartCount]=${filePart}
	filePartCount=$(($filePartCount+1))	

done

rm -f $orgDatasetFile/*.nt  2>/dev/null #Rimuovo i file generati nell'esecuzione precedente
mkdir -p $orgDatasetFile

#Processi da eseguire per l'organizzazione (Assumo che le stringhe di file abbiano almeno un file)
orgFile[0]="gawk -f $AwkScript -v prefix=1 -v destinatioDirectory=\"${orgDatasetFile}\" ${filePartCom[0]}"
orgFile[1]="gawk -f $AwkScript -v prefix=2 -v destinatioDirectory=\"${orgDatasetFile}\" ${filePartCom[1]}"
orgFile[2]="gawk -f $AwkScript -v prefix=3 -v destinatioDirectory=\"${orgDatasetFile}\" ${filePartCom[2]}"
orgFile[3]="gawk -f $AwkScript -v prefix=4 -v destinatioDirectory=\"${orgDatasetFile}\" ${filePartCom[3]}"

#Rinizializzo le variabili della parallelizzazione, per sicurezza
NUM=0
QUEUE=""

#Avvio l'esecuzione parallela dei processi
for (( proc=0; proc<${#orgFile[@]}; proc++ )) # for the rest of the arguments
do
	#echo ${orgFile[$proc]}
	eval ${dbgCmd}""${orgFile[$proc]} &
	PID=$!
	queue $PID

	while [ $NUM -ge $NProc ]; do
		checkqueue
		sleep 0.4
	done
done
wait # attendi il completamento di tutti i processi prima di procedere con il passo successivo
sync #Mi assicuro che tutte le informazioni siano scritte su file

#Rimuovo le singole parti (Separato perchè in parallelo si crea dipendenza tra coppie di processi, ed essendo la rimozione veloce si può gestire senza problemi così)
rm -f $tmpDatasetFile/1_lod_part_aa
rm -f $tmpDatasetFile/1_lod_part_ab
rm -f $tmpDatasetFile/1_lod_part_ac
rm -f $tmpDatasetFile/1_lod_part_ad

rm -rf $tmpDatasetFile/ #Rimuovo la directory con i file temporanei, non più utili

endBlock=$SECONDS
if [ $debug -eq 1 ]
then
	echo "Time: $((endBlock - startBlock)) secs."
	echo ""
fi

echo "---End: Organize and Split files---"
echo ""
echo "---Start: Deduplication of files---"

startBlock=$SECONDS

#Creo i processi che andranno ad unire i file (2>/dev/null per non scrivere errori di file non esistenti, perchè il cat funziona comunque su tutti quelli che ci sono)
numMerge=0
for element in "${splitters[@]}"
do
   #TODO: Per generalizzare, bisogna verificare se i file ci sono e creare dinamicamente il comando perchè cat crea il file vuoto anche se i file sorgente non vi sono
   #Unisco i file types
   mergeFile[$numMerge]="cat ${orgDatasetFile}/1${element}_types.nt ${orgDatasetFile}/2${element}_types.nt ${orgDatasetFile}/3${element}_types.nt ${orgDatasetFile}/4${element}_types.nt > ${orgDatasetFile}/${element}_types.nt 2>/dev/null"
   numMerge=$(($numMerge+1))
   #Unisco i file obj_properties
   mergeFile[$numMerge]="cat ${orgDatasetFile}/1${element}_obj_properties.nt ${orgDatasetFile}/2${element}_obj_properties.nt ${orgDatasetFile}/3${element}_obj_properties.nt ${orgDatasetFile}/4${element}_obj_properties.nt > ${orgDatasetFile}/${element}_obj_properties.nt 2>/dev/null"
   numMerge=$(($numMerge+1))
   #Unisco i file dt_properties
   mergeFile[$numMerge]="cat ${orgDatasetFile}/1${element}_dt_properties.nt ${orgDatasetFile}/2${element}_dt_properties.nt ${orgDatasetFile}/3${element}_dt_properties.nt ${orgDatasetFile}/4${element}_dt_properties.nt > ${orgDatasetFile}/${element}_dt_properties.nt 2>/dev/null"
   numMerge=$(($numMerge+1))
done

#Unisco i file
#Rinizializzo le variabili della parallelizzazione, per sicurezza
NUM=0
QUEUE=""

#Avvio l'esecuzione parallela dei processi
for (( proc=0; proc<${#mergeFile[@]}; proc++ )) # for the rest of the arguments
do
	#echo ${mergeFile[$proc]}
	eval ${dbgCmd}""${mergeFile[$proc]} &
	PID=$!
	queue $PID

	while [ $NUM -ge $NProc ]; do
		checkqueue
		sleep 0.4
	done
done
wait # attendi il completamento di tutti i processi prima di procedere con il passo successivo
sync #Mi assicuro che tutte le informazioni siano scritte su file

#Rimuovo le singole parti (Separato perchè in parallelo si crea dipendenza tra coppie di processi, ed essendo la rimozione veloce si può gestire senza problemi così)
for i in 1 2 3 4
do
	for element in "${splitters[@]}"
	do
	   #Elimino i file types
	   rm -f ${orgDatasetFile}/${i}${element}"_types.nt"
	   #Elimino i file obj_properties
	   rm -f ${orgDatasetFile}/${i}${element}"_obj_properties.nt"
	   #Elimino i file dt_properties
	   rm -f ${orgDatasetFile}/${i}${element}"_dt_properties.nt"
	done
done

#Creo i processi che andranno a rimuovere i duplicati
numDedupl=0
for element in "${splitters[@]}"
do
   #Elimino i duplicati dai file types
   if [ -f ${orgDatasetFile}/${element}_types.nt ];
   then
	   deduplFile[$numDedupl]="sort -u ${orgDatasetFile}/${element}_types.nt -o ${orgDatasetFile}/${element}_types.nt"
	   numDedupl=$(($numDedupl+1))
   fi
   #Elimino i duplicati dai file obj_properties
   if [ -f ${orgDatasetFile}/${element}_obj_properties.nt ];
   then
	   deduplFile[$numDedupl]="sort -u ${orgDatasetFile}/${element}_obj_properties.nt -o ${orgDatasetFile}/${element}_obj_properties.nt"
	   numDedupl=$(($numDedupl+1))
   fi
   #Elimino i duplicati dai file dt_properties
   if [ -f ${orgDatasetFile}/${element}_dt_properties.nt ];
   then
	   deduplFile[$numDedupl]="sort -u ${orgDatasetFile}/${element}_dt_properties.nt -o ${orgDatasetFile}/${element}_dt_properties.nt"
	   numDedupl=$(($numDedupl+1))
   fi
done

#Rinizializzo le variabili della parallelizzazione, per sicurezza
NUM=0
QUEUE=""

#Avvio l'esecuzione parallela dei processi
for (( proc=0; proc<${#deduplFile[@]}; proc++ )) # for the rest of the arguments
do
	#echo ${deduplFile[$proc]}
	eval ${dbgCmd}""${deduplFile[$proc]} &
	PID=$!
	queue $PID

	while [ $NUM -ge $NProc ]; do
		checkqueue
		sleep 0.4
	done
done
wait # attendi il completamento di tutti i processi prima di procedere con il passo successivo
sync #Mi assicuro che tutte le informazioni siano scritte su file

echo "---End: Deduplication of files---"
echo ""