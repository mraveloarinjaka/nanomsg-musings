(ns nano.core
  (:require [nanomsg.core :as nn]
            [byte-streams :as bs])  
  (:gen-class))

(defn startPublisher
  [endPoint]
  (with-open [sock (nn/socket :pub {:bind endPoint})]
    (loop []
      (Thread/sleep 1000)
      (nn/send! sock "This is a publication")
      (recur))))

(defn startSubscriber
  [endPoint topic]
  (with-open [sock (nn/socket :sub)]
    (nn/connect! sock endPoint)
    (nn/subscribe! sock topic)
    (dotimes [i 3]
      (println (bs/convert (nn/recv! sock) String)))))

;(defn startServer
;  [endPoint]
;  (with-open [sock (nn/socket :rep {:bind endPoint})]
;    (loop []
;      (nn/send! sock (nn/recv! sock))
;      (recur))))

;(defn startClient
;  [endPoint]
;  (with-open [sock (nn/socket :req {:bind endPoint})]
;    (dotimes [i 5]
;      (nn/send! sock (str "msg:" 1))
;      (println "Received:" (nn/recv! sock)))))

;(def PUB_END_POINT "tcp://*:2714")
;(def SUB_END_POINT "tcp://localhost:2714")
;(def PUB_END_POINT "inproc://my-endpoint")
;(def SUB_END_POINT "inproc://my-endpoint")
(def PUB_END_POINT "ipc://my-endpoint")
(def SUB_END_POINT "ipc://my-endpoint")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (do
    (future (startPublisher PUB_END_POINT))
    (future (startSubscriber SUB_END_POINT "This"))
    (future (startSubscriber SUB_END_POINT "Other"))
    (future (startSubscriber SUB_END_POINT "This is"))
    (println "done")))

