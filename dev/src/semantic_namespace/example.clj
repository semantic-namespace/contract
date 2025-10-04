(ns semantic-namespace.example
  (:require [semantic-namespace.contract :as contract]
            [semantic-namespace.contract.type :as contract.type]))

(contract/def
  :semantic-namespace.contract/docs
  :app.cool/stuff
  {:semantic-namespace.docs/content "Hello app cool stuff docs!"})

;; {:semantic-spec/subject :app.cool/stuff,
;;  :semantic-spec.relationship/id :semantic-spec/docs,
;;  :semantic-spec.relationship/values
;;  #:semantic-spec.docs{:content "Hello app cool stuff docs!"}}

(contract/fetch :semantic-namespace.contract/docs :app.cool/stuff)


(contract.type/instances  {:semantic-namespace.contract.type/id :semantic-namespace.contract/docs})


(contract/props :semantic-namespace.contract/docs)

(mapv (partial contract/fetch :semantic-namespace.contract/docs)
      (contract.type/instances {:semantic-namespace.contract.type/id :semantic-namespace.contract/docs}))


(contract/def
  :semantic-namespace.contract/docs
  :app.feature/foo
  {:semantic-namespace.docs/content "app feature foo  docs!"})

(mapv (partial contract/fetch :semantic-namespace.contract/docs) (contract.type/instances {:semantic-namespace.contract.type/id :semantic-namespace.contract/docs}))


