package mq;

import javax.jms.JMSException;
import java.util.ArrayList;

public interface MQMTInt {
  /**
   * Producimos y consumimos lanzando un hilo por cada productor y cada consumidor
   * @param nMsg número total de mensajes a producir
   * @param topic nombre de la cola
   */
  void produceAndConsumeMT(int nMsg, String topic, int nProd, int nCons) throws JMSException, InterruptedException;

  /**
   * Devuelve una lista de hilos lanzados que insertarán mensajes en la cola
   *
   * @param nMsg número total de mensajes a insertar
   * @param nProd número total de productores a distribuir los mensajes
   * @param topic nombre de la cola
   * @return ArrayList de hilos ya lanzados
   */
  ArrayList<Thread> produceMT(int nMsg, int nProd, String topic);

  /**
   * Devuelve una lista de hilos lanzados que extraerán mensajes en la cola
   *
   * @param nMsg número total de mensajes a extraer
   * @param nCons número total de consumidores a distribuir los mensajes a extraer
   * @param topic nombre de la cola
   * @return ArrayList de hilos ya lanzados
   */
  ArrayList<Thread> consumeMT(int nMsg, int nCons, String topic);
}
