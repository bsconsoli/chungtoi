all:			Make

Make:
		@javac --add-modules java.xml.ws ../chungtoi/*.java

clean:
			@rm -f *.class *~

info:
			@echo "(c) Bernardo S. Consoli (Novembro 2017)"

