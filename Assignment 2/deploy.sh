echo "  > Enviar JARs para as diferentes maquinas"
(cd JARS;
sshpass -p qwerty scp -o StrictHostKeyChecking=no Repository.jar sd0201@l040101-ws01.ua.pt:~
sshpass -p qwerty scp -o StrictHostKeyChecking=no Control.jar sd0201@l040101-ws09.ua.pt:~
sshpass -p qwerty scp -o StrictHostKeyChecking=no Concentration.jar sd0201@l040101-ws03.ua.pt:~
sshpass -p qwerty scp -o StrictHostKeyChecking=no Party.jar sd0201@l040101-ws04.ua.pt:~
sshpass -p qwerty scp -o StrictHostKeyChecking=no Party2.jar sd0201@l040101-ws05.ua.pt:~
sshpass -p qwerty scp -o StrictHostKeyChecking=no Museum.jar sd0201@l040101-ws06.ua.pt:~
sshpass -p qwerty scp -o StrictHostKeyChecking=no Thieves.jar sd0201@l040101-ws07.ua.pt:~
sshpass -p qwerty scp -o StrictHostKeyChecking=no Master.jar sd0201@l040101-ws10.ua.pt:~
)