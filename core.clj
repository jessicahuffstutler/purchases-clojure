(ns purchases-clojure.core
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [clojure.pprint :as pp])
  (:gen-class))

(defn -main [& args]
  (println "Type one of the following categories and hit enter to filter:
  Furniture, Alcohol, Toiletries, Shoes, Food, Jewelry")
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
        input (read-line)
        purchases (filter (fn [line]
                              (= input (:category line)))
                            purchases)]
    (spit "filtered_purchases.edn"                          ;"(spit (format "filetered_%s.edn" input)" would save the file as filtered_Alcohol.edn for ex.
          (with-out-str (pp/pprint purchases)))))           ;makes the saved file easier to read than the previous code "(pr-str purchases)"
