## Weather forecast application

Command below should be run in source code root. 

# To Compile
mvn clean package

# To run
java -jar target/forecast-0.1-SNAPSHOT.jar  --mintemp=0 --maxtemp=30 --locations=lagos,Helsinki,talinn --interval.sec=30

#View log
tail -f applogs/forecast.log

#Ui 
The ui can be access http://localhost:8080/ui

