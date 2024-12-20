(ns game.core
  (:require ["phaser" :refer [AUTO Game Scale Math]]
            [goog.object :as obj]))

(defonce state (atom {}))

(defn game-preload []
  (this-as ^js/Object this
    (-> this .-load (.image "block" "assets/sprites/crate32.png"))
    (-> this .-load (.image "ice" "assets/sprites/block-ice.png"))
    (-> this .-load (.image "be" "assets/sprites/beball1.png"))))

(defn game-create []
  (this-as ^js/Object this
    (let [cursors (-> this .-input .-keyboard (.createCursorKeys))
          player (doto (-> this .-physics .-add (.sprite 400 300 "be"))
                   (.setName "player")
                   (.setCollideWorldBounds true)
                   (.setPushable false))
          blocks (for [_ (range 16)
                       :let [x (.Between Math 0 800)
                             y (.Between Math 0 600)]]
                   (doto (-> this .-physics .-add (.sprite x y "block"))
                     (.setCollideWorldBounds true)
                     (-> .-body .-slideFactor (.set 0 0))))
          ices (for [_ (range 16)
                     :let [x (.Between Math 0 800)
                           y (.Between Math 0 600)]]
                 (doto (-> this .-physics .-add (.sprite x y "ice"))
                   (.setScale 0.125)
                   (.setCollideWorldBounds true)
                   (.setDrag 100)
                   (.setBounce 1)))
          boxes (clj->js (into blocks ices))]
      (-> this .-physics .-add
          (.collider player boxes nil (fn [_player ^js/Object box]
                                        (if (:playerIsNPC @state)
                                          (.setPushable box false)
                                          (.setPushable box true))
                                        true)))
      (swap! state assoc :playerIsNPC false)
      (-> this .-input
          (.on "pointerdown" (fn []
                               (swap! state update :playerIsNPC not)
                               (if (:playerIsNPC @state)
                                 (.setTint player 0xff0000)
                                 (.clearTint player)))))
      (obj/set this "player" player)
      (obj/set this "cursors" cursors))))

(defn game-update []
  (this-as ^js/Object this
    (let [player (obj/get this "player")
          cursors (obj/get this "cursors")]
      (-> player (.setVelocity 0 0))
      (cond
        (-> cursors .-left .-isDown) (.setVelocityX player -400)
        (-> cursors .-right .-isDown) (.setVelocityX player 400))
      (cond
        (-> cursors .-up .-isDown) (.setVelocityY player -400)
        (-> cursors .-down .-isDown) (.setVelocityY player 400)))))

(def config {:type AUTO
             :parent "game"
             :width 800
             :height 600
             :physics {:default "arcade"
                       :arcade {:gravity {:y 0}
                                :debug true}}
             :scale {:mode (.-FIT Scale)
                     :autoCenter (.-CENTER_BOTH Scale)}
             :scene [{:key "main-scene"
                      :preload game-preload
                      :create game-create
                      :update game-update}]})

(defn ^:export init []
  (when-let [^js/Object game (:game @state)]
    (.destroy game true))
  (reset! state {:game (new Game (clj->js config))}))

(init)
