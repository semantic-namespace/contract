(ns semantic-namespace.contract-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :refer (deftest testing is)]
            [semantic-namespace.contract :as contract]
            [semantic-namespace.contract.type :as contract.type])
  (:import [clojure.lang ExceptionInfo]))

(deftest def-test
  (testing "define specs, predicate and triple"

    (s/def ::bar string?)
    (s/def ::zoo int?)

    (testing "defining contract type"
      (contract.type/def ::context [::bar ::zoo]))

    (testing "redefining contract type with same props"
      (contract.type/def ::context [::bar ::zoo]))

    (testing "redefining contract type with different props throws exception"
      (s/def ::pop uuid?)
      (try
        (contract.type/def ::context [::bar ::zoo ::pop])
        (is false "exception should happen for redef with diffs props")
        (catch ExceptionInfo e
          (is (= ::context (:id (ex-data e)))))))
    
    (let [relationship-values {::bar "juan" ::zoo 14}]
      (is (= (contract/def ::context ::subject relationship-values)
             [::context ::subject]))
      (is (= (contract/fetch ::context ::subject)
             {:semantic-namespace.contract/type :semantic-namespace.contract-test/context
              :semantic-namespace.contract/instance :semantic-namespace.contract-test/subject
              :semantic-namespace.contract-test/bar "juan"
              :semantic-namespace.contract-test/zoo 14})))

    (testing "invalid spec props throws exception ... ::zoo should be int"
      (try
       (contract/def ::context ::subject {::bar "juan" ::zoo "14"})
       (is false "exception should happen for invalid spec")
       (catch ExceptionInfo  e
         (is (= ::zoo (:spec (ex-data e)))))))
    (testing "props don't exist throws exception ... ::bar should be exist (and be valid)"
      (try
       (contract/def ::context ::subject {::zoo "14"})
       (is false "exception should happen for unexistent spec")
       (catch ExceptionInfo  e
         (is (= ::bar (:spec (ex-data e)))))))))
