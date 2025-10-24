(ns semantic-namespace.example
  (:require [semantic-namespace.contract :as contract]
            [clojure.spec.alpha :as s]
            [semantic-namespace.contract.type :as contract.type]))

(s/def :docs/content string?)
;; => :docs/content

(contract.type/def #{:semantic-namespace/docs} [:docs/content] true)
;; => [#{:semantic-namespace/docs} [:docs/content]]

(contract/def
  #{:semantic-namespace/docs :app.cool/stuff}
  {:docs/content "Hello app cool stuff docs!"})
;; => [#{:semantic-namespace/docs
;;    :semantic-namespace.contract/instance
;;    :app.cool/stuff}
;;  [:docs/content]]

(contract/fetch #{:semantic-namespace/docs :app.cool/stuff})
;; => #:docs{:content "Hello app cool stuff docs!"} 

(contract.type/instances  #{:semantic-namespace/docs})
;; => [#{:semantic-namespace/docs :app.cool/stuff}]

(contract.type/props #{:semantic-namespace/docs} )
;; =>  #:semantic-namespace.spec.keys{:specs [:docs/content]}

(mapv  contract/fetch 
      (contract.type/instances #{:semantic-namespace/docs}))
;; => [#:docs{:content "Hello app cool stuff docs!"}] 

(contract/def
  #{:semantic-namespace/docs :app.feature/foo}
  {:docs/content "app feature foo  docs!"})
;; [#{:semantic-namespace/docs
;;    :semantic-namespace.contract/instance
;;    :app.feature/foo}
;;  [:docs/content]]


(mapv contract/fetch  (contract.type/instances #{:semantic-namespace/docs}))
;; [#:docs{:content "Hello app cool stuff docs!"}
;;  #:docs{:content "app feature foo  docs!"}]

