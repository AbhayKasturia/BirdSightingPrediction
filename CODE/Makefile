DIR := $(shell basename `pwd`)

$(DIR).jar: *.java build.gradle Makefile
	gradle build
	gradle shadowJar
	cp build/libs/$(DIR)-all.jar $(DIR).jar

run: $(DIR).jar
	hadoop jar $(DIR).jar /home/abhay/Documents/MR/project/CODE/labelled /home/abhay/Documents/MR/project/CODE/output /home/abhay/Documents/MR/project/CODE/output/val 10 /home/abhay/Documents/MR/project/CODE/unlabelled output_final output_val

clean-output: 
	rm -rf output

clean:
	rm -rf build bin *.jar .gradle test.log
	clear

clean-all:
	rm -rf build bin *.jar .gradle test.log
	rm -rf output
	rm -rf model
	rm -rf output_val
	clear