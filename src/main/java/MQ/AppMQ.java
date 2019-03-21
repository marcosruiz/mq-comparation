public abstract class AppCommon {
  /**
   * Muestra por la salida estandar algunos stats
   * @param timeStart
   * @param nMsg
   */
  private static void printStats(long timeStart, int nMsg) {
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
}
