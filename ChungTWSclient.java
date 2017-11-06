/**
 * @file ChungTWSclient
 * @author Roland Teodorowitsch
 * @date November 2017
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ChungTWSclient {

    static ChungTWS port;
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        ChungTWS_Service service = new ChungTWS_Service();
        port = service.getChungTWSPort();
        executaTeste("ChungToi-0000");
    }

    private static int preRegistro(java.lang.String j1, int id1, java.lang.String j2, int id2) {
        return port.preRegistro(j1, id1, j2, id2);
    }
    
    private static void executaTeste(String rad) throws IOException {
        String inFile = rad+".in";
        FileInputStream is = new FileInputStream(new File(inFile));
        System.setIn(is);

        String outFile = rad+".out";
        FileWriter outWriter = new FileWriter(outFile);
        try (PrintWriter out = new PrintWriter(outWriter)) {
            Scanner leitura = new Scanner(System.in);
            int numOp = leitura.nextInt();
            for (int i=0;i<numOp;++i) {
                System.out.print("\r"+rad+": "+(i+1)+"/"+numOp);
                int op = leitura.nextInt();
                String parametros = leitura.next();
                String param[] = parametros.split(":",-1);
                switch(op) {
                    case 0:
                        if (param.length!=4)
                            erro(inFile,i+1);
                        else
                            out.println(port.preRegistro(param[0],Integer.parseInt(param[1]),param[2],Integer.parseInt(param[3])));
                        break;
                    case 1:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.registraJogador(param[0]));
                        break;
                    case 2:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.encerraPartida(Integer.parseInt(param[0])));
                        break;
                    case 3:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.temPartida(Integer.parseInt(param[0])));
                        break;
                    case 4:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemOponente(Integer.parseInt(param[0])));
                        break;
                    case 5:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.ehMinhaVez(Integer.parseInt(param[0])));
                        break;
                    case 6:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemTabuleiro(Integer.parseInt(param[0])));
                        break;
                    case 7:
                        if (param.length!=3)
                            erro(inFile,i+1);
                        else
                            out.println(port.posicionaPeca(Integer.parseInt(param[0]),Integer.parseInt(param[1]),Integer.parseInt(param[2])));
                        break;
                    case 8:
                        if (param.length!=5)
                            erro(inFile,i+1);
                        else
                            out.println(port.movePeca(Integer.parseInt(param[0]),Integer.parseInt(param[1]),Integer.parseInt(param[2]),Integer.parseInt(param[3]),Integer.parseInt(param[4])));
                        break;
                    default:
                        erro(inFile,i+1);
                }
            }
            System.out.println("... terminado!");
            out.close();
            leitura.close();
        }
    }
    
    private static void erro(String arq,int operacao) {
        System.err.println("Entrada invalida: erro na operacao "+operacao+" do arquivo "+arq);
        System.exit(1);
    }
    
}
