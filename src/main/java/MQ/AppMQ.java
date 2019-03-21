package MQ;

import javax.jms.JMSException;
import java.util.ArrayList;

public abstract class AppMQ {
  /**
   * Muestra por la salida estandar algunos stats
   * @param timeStart
   * @param nMsg
   */
  protected static void printStats(long timeStart, int nMsg) {
    // Tiempo y velocidad
    long timeEnd = System.currentTimeMillis();
    double diffMillis = timeEnd - timeStart;
    double diffSec = diffMillis / 1000;
    double vel = nMsg / diffSec;
    System.out.println("Tiempo total: " + diffSec + " seconds");
    System.out.println("Velocidad: " + vel + " msg/seg");

    // Memoria RAM
    long totalMem = Runtime.getRuntime().totalMemory();
    long freeMem = Runtime.getRuntime().freeMemory();
    long usedMem = totalMem-freeMem;
    long maxMem = Runtime.getRuntime().maxMemory();
    System.out.println("Memory used: " + usedMem);
    System.out.println("Memory max: " + maxMem);
  }

//  public abstract void produce(int nMsg, int nProd, String topic);
//  public abstract void consume(int nMsg, int nProd, String topic);
//  public abstract ArrayList<Thread> produceT(int nMsg, String topic);
//  public abstract ArrayList<Thread> consumeT(int nMsg, String topic);
//  public abstract void mainSecuential(int nMsg, String topic);
//  public abstract void mainMultithread(int nMsg, String topic, int nProd, int nCons);
}
