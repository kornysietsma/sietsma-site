(ns sietsma-site.layout
  (:require [hiccup.page :refer [html5]]
            [optimus.link :as link]))

(def default-title "Sietsma Family Website")

(def fonts [
            "http://fonts.googleapis.com/css?family=Cousine:400,700,400italic,700italic&subset=latin,latin-ext"
            "http://fonts.googleapis.com/css?family=Vollkorn:400italic,700italic,400,700', :rel => 'stylesheet"
            ;"http://fonts.googleapis.com/css?family=Playfair+Display:400,700,900,400italic,700italic,900italic"
            ])

(defn wrap-layout [title content request]
  (html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name    "viewport"
             :content "width=device-width, initial-scale=1.0"}]
     [:meta {:content "IE=edge,chrome=1" :http-equiv "X-UA-Compatible"}]
     [:meta {:name    "description"
             :content ""}]
     [:title title]
     [:link {:rel "icon" :href "/favicon.ico" :type "image/ico"}]
     [:link {:rel "shortcut" :href "/favicon.ico" :type "image/ico"}]
     ;[:script {:src (link/file-path request "/scripts/vendor/kraken/detects/svg.js")}]
     (for [font fonts]
       [:link {:rel "stylesheet" :href font}])
     [:link {:rel "stylesheet" :href (link/file-path request "/styles/site.scss")}]]
    content))

(defn default-theme [data]
  [:body {:class "default"}
   [:section.container
    (:page data)]])

(defn wedding-theme [{:keys [page class title tab] :or {class "wedding"}}]
  (let [index-class (if (= tab "index") {:class "current"} {})
        uk-class (if (= tab "uk") {:class "current"} {})
        australia-class (if (= tab "australia") {:class "current"} {})
        registry-class (if (= tab "registry") {:class "current"} {})
        photos-class (if (= tab "photos") {:class "current"} {})
        faq-class (if (= tab "faq") {:class "current"} {})
        ]
    [:body {:class class}
     [:header
      [:h1 title]
      [:nav [:ul
             [:li [:a (merge index-class {:href "/wedding"}) "Home"]]
             [:li [:a (merge uk-class {:href "/wedding/uk.html"}) "UK"]]
             [:li [:a (merge australia-class {:href "/wedding/australia.html"}) "Australia"]]
             [:li [:a (merge registry-class {:href "/wedding/registry.html"}) "Gift registry"]]
             [:li [:a (merge photos-class {:href "/wedding/photos.html"}) "Photography"]]
             [:li [:a (merge faq-class {:href "/wedding/faq.html"}) "Frequently asked questions"]]
             ]]]
     [:section.container
      ;[:aside "This site is still a bit unfinished, we're updating it as we find time! Feel free to contact us with any questions"]
      page]]))

(defn theme-page [{:keys [theme title page]
                   :or   {title default-title }
                   :as data}
                  request]
  (let [content (case theme
          "wedding" (wedding-theme data)
          (default-theme data))]
    (wrap-layout title content request)))


