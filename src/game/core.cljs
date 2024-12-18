(ns game.core
  (:require
   ["phaser" :refer [Game AUTO Scale]]))

(defonce state (atom {}))

(defn game-preload []
  (this-as this
           (-> this .-load (.image "sky" "assets/sky.png"))))

(defn game-create []
  (this-as this
             (-> this .-add (.image 400 300 "sky"))))

(defn game-update [])

(def config {:type AUTO
             :parent "game"
             :width 800
             :height 600
             :scale {:mode (.-FIT Scale)
                     :autoCenter (.-CENTER_BOTH Scale)}
             :scene {:preload game-preload
                     :create game-create
                     :update game-update}})

(defn- prepare-container! [id]
  (let [container (js/document.getElementById id)]
    (set! (.-innerHTML container) "")
    container))

(defn ^:export init []
  (prepare-container! "game")
  (reset! state {})
  (swap! state assoc
         :game (new Game (clj->js config))))

(init)
