(ns semantic-namespace.contract
  (:require [clojure.spec.alpha :as s]
            [semantic-namespace.contract.type :as contract.type]))

(defonce registry (ref {}))

(defn props
  [type]
  (::contract.type/props (type @contract.type/registry)))


(defn def
  "register a contract type relation for a specific instance and contract-props"
  [type instance props]
  (let [data {:semantic-namespace.contract/type type
              :semantic-namespace.contract/instance instance
              :semantic-namespace.contract/props props}
        type-keys (props type)]
    (assert (type @contract.type/registry) "type should be defined as contract.type/def before using this contract/def")
    (mapv (fn [type-key]
            (when-not (s/valid? type-key (type-key props))
              (throw (ex-info (format "Invalid data asserted: %s" (s/explain-str type-key (type-key props)))
                              data))))
          type-keys)
    (dosync
     (alter registry update instance merge props)
     (alter contract.type/registry update-in [type ::contract.type/instances] conj instance)
     data)))

(defn fetch
  [type instance]
  (-> @registry instance (select-keys (props type))))
