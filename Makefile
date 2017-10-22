all:			ChungClient.class ChungServer.class \
				ChungImpl.class ChungInterface.class \
				ChungPlayer.class ChungToi.class

ChungImpl.class:	ChungImpl.java ChungInterface.class
			@javac ChungImpl.java

ChungInterface.class:	ChungInterface.java
			@javac ChungInterface.java

ChungClient.class:	ChungClient.java
			@javac ChungClient.java

ChungServer.class:	ChungServer.java
			@javac ChungServer.java
			
ChungToi.class:	ChungToi.java
			@javac ChungToi.java
			
ChungPlayer.class:	ChungPlayer.java
			@javac ChungPlayer.java

clean:
			@rm -f *.class *~

info:
			@echo "(c) Bernardo S. Consoli (Outubro 2017)"

