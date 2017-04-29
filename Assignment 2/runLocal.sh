echo "  > Correr JARs na maquina local"
(cd JARS;
x-terminal-emulator -e java -jar Repository.jar
x-terminal-emulator -e java -jar Control.jar
x-terminal-emulator -e java -jar Concentration.jar
x-terminal-emulator -e java -jar Party.jar
x-terminal-emulator -e java -jar Party2.jar 
x-terminal-emulator -e java -jar Museum.jar 
x-terminal-emulator -e java -jar Thieves.jar
sleep 1; x-terminal-emulator -e java -jar Master.jar  
)