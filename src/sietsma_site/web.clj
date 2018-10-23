(ns sietsma-site.web
  (:require [ring.middleware.content-type :refer [wrap-content-type]]
            [stasis.core :as stasis]
            [optimus.prime :as optimus]
            [optimus.assets :as assets]
            [optimus-sass.core]
            [optimus.export]
            [hiccup.page :refer [html5]]
            [optimus.optimizations :as optimizations]
            [optimus.strategies :refer [serve-live-assets]]
            [sietsma-site.pages :as pages]
            [sietsma-site.highlight :refer [highlight-code-blocks]]))

(defn get-assets []
  (assets/load-assets "public" [;#"/styles/.*\.css"
                                #"/styles/[^_]*\.scss"
                                #"/images/.*"
                                #"/scripts/.*\.js"
                                ]))

(defn prepare-page [page req]
  (if (string? page)
    page
    (page req)))

(defn prepare-pages [pages]
  (into {}
        (for [[name page] pages]
          [name (partial prepare-page page)])))

(defn publics []
  (stasis/slurp-directory "resources/public" #".*\.(html|svg|js)$"))

(defn get-raw-pages []
  (stasis/merge-page-sources
    {                                                       ; :partials (pages/get-partial-pages)
     :markdown (pages/get-markdown-pages)
     :hiccup (pages/get-hiccup-pages)
     :public (publics)}))

(defn get-pages []
  (prepare-pages (get-raw-pages)))

(def optimize optimizations/all)

(def app (-> (stasis/serve-pages get-pages)
             (optimus/wrap get-assets optimizations/none serve-live-assets)
             wrap-content-type))

(def export-dir "./docs")

(defn export []
  (let [assets (optimize (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages) export-dir {:optimus-assets assets})
    (spit (str export-dir "/CNAME") (slurp "resources/public/CNAME"))))
