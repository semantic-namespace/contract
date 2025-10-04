(ns semantic-namespace.contract-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer (deftest testing is)]
            [semantic-namespace.contract :as contract]
            [semantic-namespace.contract.type :as contract.type]))

(deftest def-test
  (testing "define specs, predicate and triple"

    (s/def ::bar string?)
    (s/def ::zoo int?)

    (contract.type/def ::context [::bar ::zoo])

    (let [relationship-values {::bar "juan" ::zoo 14}]
      (is (= (contract/def
               ::context
               ::subject
               relationship-values)
             {::contract/instance ::subject
              ::contract/type ::context
              ::contract/props relationship-values})))))
