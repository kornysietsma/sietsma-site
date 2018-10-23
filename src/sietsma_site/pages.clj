(ns sietsma-site.pages
  (:require [hiccup.page :refer [html5]]
            [stasis.core :as stasis]
            [optimus.link :as link]
            [sietsma-site.layout :as layout]
            [me.raynes.cegdown :as md]
            [sietsma-site.frontmatter :as fm]))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks
   :abbreviations
   :definitions
   ;:anchorlinks                                             ; not working?
   ;:hardwraps
   :tables
   :quotes
   :smarts
   :smartypants
   :fenced-code-blocks
   :wikilinks
   :strikethrough
   ])


#_(defn partial-pages [pages]
  (into {}
        (for [[name page] pages]
          [name
           (partial layout/theme-page {:page page})])))

(defn fix-markdown-filename [name]
  (if (re-matches #".*/index\.md$" name)
    (clojure.string/replace name #"index\.md$" "")
    (clojure.string/replace name #"\.md$" ".html")))

(defn parse-markdown-frontmatter [file-contents]
  (let [{:keys [frontmatter body]} (fm/parse-str file-contents)]
    (merge frontmatter
           {:page  (md/to-html body pegdown-options)})
    ))

(defn markdown-pages [pages]
  (into {}
        (for [[name page] pages]
          [(fix-markdown-filename name)
           (partial layout/theme-page (parse-markdown-frontmatter page ))])))

#_(defn get-partial-pages []
  (partial-pages (stasis/slurp-directory "resources/partials" #".*\.html$")))

(defn get-markdown-pages []
  (markdown-pages (stasis/slurp-directory "resources/pages" #"\.md$")))

(defn fix-hiccup-filename [name]
  (if (re-matches #".*/index\.hiccup$" name)
    (clojure.string/replace name #"index\.hiccup$" "")
    (clojure.string/replace name #"\.hiccup$" ".html")))

(defn hiccup-pages [pages]
  (into {}
        (for [[name page] pages]
          [(fix-hiccup-filename name)
           (partial layout/theme-page (read-string page))])))

(defn get-hiccup-pages []
  (hiccup-pages (stasis/slurp-directory "resources/pages" #"\.hiccup$")))