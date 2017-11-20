# chungtoi

To compile:
    In the chungtoi folder:
	    make java8, caso o jdk instalado no computador seja versão 8
	    make java9, caso o jdk instalado no computador seja versão 9

To run:
	Publish service:
        In the folder containing the chungtoi folder:
            Java 8: java chungtoi.ChungServerPublisher [IP address of Service]
            Java 9: java --add-modules java.xml.ws chungtoi.ChungServerPublisher [IP address of Service]

    Run client:
        In the folder containing the chungtoi folder:
            Java 8: java chungtoi.ChungTWSclient [IP address of Service] [.in file]
            Java 9: java --add-modules java.xml.ws chungtoi.ChungTWSclient [IP address of Service] [.in file]
