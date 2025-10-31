(ns semantic-namespace.contract
  (:require [clojure.spec.alpha :as s]
            [clojure.set :as set]
            [semantic-namespace.compound.identity :as compound.identity]))


(defn get-id [id]
  (into compound.identity/env-id (if (coll? id) id #{id})))

(defn def
  "register a contract type relation for a specific instance and contract-props"
  [id props]
  (let [id (get-id id)
        type-keys (let [res (->> (keys @compound.identity/registry)
                                 (filter :semantic-namespace.contract/type)
                                 (filter (comp not :semantic-namespace.contract/instance))
                                 (filter #(= #{} (set/difference % (conj id :semantic-namespace.contract/type)))))]
                    (assert (= 1 (count res))  {:compound-identity id
                                                :res res
                                                :message (format "contract type not unique or not found for [%s]. Found counter: %s" id (count res))})
                    (let [contract-type (first res)]
                      (:semantic-namespace.spec.keys/specs (get @compound.identity/registry contract-type))))]
    (assert type-keys "type should be defined as contract.type/def before using this contract/def")
    ;; validation using clojure.spec
    (mapv (fn [type-key]
            (when-not (s/valid? type-key (type-key props))
              (throw (ex-info (format "Invalid data for : %s" type-key)
                              {:spec-explain-data (s/explain-data type-key (type-key props))
                               :spec type-key
                               :spec-value (type-key props)}))))
          type-keys)
    ;; if no errors registry mutate!
    (swap! compound.identity/registry assoc (conj id :semantic-namespace.contract/instance) props)
    ;; return easy to read data
    [(conj id :semantic-namespace.contract/instance) type-keys]))

(defn fetch
  [id]
  (get @compound.identity/registry (conj (get-id id) :semantic-namespace.contract/instance)))
