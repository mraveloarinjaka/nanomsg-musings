(ns nano.core
  (:require [nanomsg.core :as nn])  
  (:gen-class))

(defn startPublisher
  [endPoint]
  (with-open [sock (nn/socket :push {:bind endPoint})]
    (doseq [name ["Foo" "Bar" "Baz"]]
      (nn/send! sock name))))

(defn startSubscriber
  [endPoint]
  (with-open [sock (nn/socket :pull)]
    (nn/connect! sock endPoint)
    (dotimes [i 3]
      (println "Hello " (nn/recv! sock)))))

(defn startServer
  [endPoint]
  (with-open [sock (nn/socket :rep {:bind endPoint})]
    (loop []
      (nn/send! sock (nn/recv! sock))
      (recur))))

(defn startClient
  [endPoint]
  (with-open [sock (nn/socket :req {:bind endPoint})]
    (dotimes [i 5]
      (nn/send! sock (str "msg:" 1))
      (println "Received:" (nn/recv! sock)))))

(def PUB_END_POINT "tcp://*:2714")
(def SUB_END_POINT "tcp://localhost:2714")
;(def PUB_END_POINT "inproc://my-endpoint")
;(def SUB_END_POINT "inproc://my-endpoint")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (do
    (println "server up")
    (.start (Thread. (startServer PUB_END_POINT)))
    (.start (Thread. (startClient SUB_END_POINT)))))
    ;(.start (Thread. (startPublisher PUB_END_POINT)))
    ;(.start (Thread. (startSubscriber SUB_END_POINT)))
    ;(.start (Thread. (startSubscriber SUB_END_POINT)))))

