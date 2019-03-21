package mq;

import javax.jms.JMSException;
import java.util.ArrayList;

public interface MQInt {
  /**
   * Producimos y consumimos con un consumidor y un productor
   * @param nMsg número total de mensajes a producir y consumir
   * @param topic nombre de la cola
   * @throws JMSException
   */
  void produceAndConsume(int nMsg, String topic) throws JMSException;

  /**
   * Inserta en la cola de mensajes especificada
   * @param nMsg número total de mensajes a insertar
   * @param topic nombre de la cola
   */
  void produce(int nMsg, String topic);

  /**
   * Extrae en la cola de mensajes especificada
   * @param nMsg número total de mensajes a insertar
   * @param topic nombre de la cola
   */
  void consume(int nMsg, String topic);

}
