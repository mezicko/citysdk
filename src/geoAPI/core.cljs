(ns geoAPI.core
  (:require [cljs.core.async
             :as async
             :refer [chan put! take! >! <! pipe timeout close! alts! pipeline-async]]
            [cljs.core.async :refer-macros [go go-loop alt!]]
            [ajax.core :as http :refer [GET POST]]
            [oops.core :as obj]
            [clojure.string :as s]
            [cljs.pprint :refer [pprint]]
            ["dotenv" :as env]
            [clojure.repl :refer [source]]
            [utils.core :as u]))

(def base-url "https://raw.githubusercontent.com/loganpowell/census-geojson/master/GeoJSON")


(defn geo-url-builder
  "Composes a URL to call raw GeoJSON files hosted on Github"
  [{:keys [vintage sourcePath geoResolution geoHierarchy]}]
  (let [[for _] (last geoHierarchy)
        {:keys [state]} geoHierarchy]
    (if (or (= state "*") (= state nil))
      (str (s/join "/" [base-url geoResolution vintage (name for)]) ".json")
      (str (s/join "/" [base-url geoResolution vintage state (name for)]) ".json"))))


;; Psedo
; create a map/index for :sourcePath -> vintage manipulation (e.g., CBP: [20)

;; Examples ====================================

(geo-url-builder {:vintage       "2016"
                  :sourcePath    ["acs" "acs5"]
                  :geoHierarchy  {:state "01"
                                  :county "*"}
                  :geoResolution "500k"
                  :values        ["B01001_001E"]})
                  ; :statsKey      stats-key})
(geo-url-builder {:vintage       "2016"
                  :sourcePath    ["acs" "acs5"]
                  :geoHierarchy  {:state "*"
                                  :county "*"}
                  :geoResolution "500k"
                  :values        ["B01001_001E"]})

(geo-url-builder {:vintage       "2016"
                  :sourcePath    ["acs" "acs5"]
                  :geoHierarchy  {:state "01"
                                  :county "001"
                                  :tract "*"}
                  :geoResolution "500k"
                  :values        ["B01001_001E"]})
;; ===============================================