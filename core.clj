(ns purchases-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [clojure.pprint :as pp]
            [ring.adapter.jetty :as j]
            [hiccup.core :as h])
  (:gen-class))

(defn read-purchases []
  (let [purchases (slurp "purchases.csv")
        purchases (str/split-lines purchases)
        purchases (map (fn [line]
                         (str/split line #","))             ;a string with the hash in front means that its a regular expression (similar to java's "\\|")
                       purchases)
        header (first purchases)
        purchases (rest purchases)
        purchases (map (fn [line]
                         (interleave header line))          ;this line could be (apply hash-map (interleave header line)) and we could remove the apply hash-map lines below.
                       purchases)
        purchases (map (fn [line]
                         (apply hash-map line))
                       purchases)
        purchases (walk/keywordize-keys purchases)
        ;input (read-line)
        ;purchases (filter (fn [line] (= input (:category line))) purchases)
        ]
    #_(spit "filtered_purchases.edn"                          ;"(spit (format "filetered_%s.edn" input)" would save the file as filtered_Alcohol.edn for ex.
          (with-out-str (pp/pprint purchases)))
    purchases))

(defn purchase-item [purchase-map]
  [:p
   "Customer ID: "
   [:c (:customer_id purchase-map)] [:br]
   [:b (:category purchase-map)]
   " "
   [:i (:credit_card purchase-map)]
   " "
   [:d (:date purchase-map)]])

(defn handler [request]
  {:status 200
   :headers {"Content-type" "text/html"}
   :body (h/html [:html
                  ;:body <- this was showing :body on my localhost page so commenting out solved that and it still displays the purchases
                  (map purchase-item (read-purchases))])})

(defn -main [& args]
  (j/run-jetty #'handler {:port 3000 :join? false}))        ;join? false means we can run this in the REPL without having to ....
