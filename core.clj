(ns purchases-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk])
  (:gen-class))

(defn -main [& args]
  (println "Type one of the following categories and hit enter to filter:
  Furniture, Alcohol, Toiletries, Shoes, Food, Jewelry")
  (let [purchases (slurp "purchases.csv")
        purchases (str/split-lines purchases)
        purchases (map (fn [line]
                         (str/split line #","))
                       purchases)
        header (first purchases)
        purchases (rest purchases)
        purchases (map (fn [line]
                         (interleave header line))
                       purchases)
        purchases (map (fn [line]
                         (apply hash-map line))
                       purchases)
        purchases (walk/keywordize-keys purchases)
        input (read-line)
        purchases (filter (fn [line]
                              (= input (:category line)))
                            purchases)
        ]
    (spit "filtered_purchases.edn"
          (pr-str purchases))))
