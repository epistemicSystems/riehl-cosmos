(ns phase1.lessons.adjoint-tour
  (:require [cljs.pprint :as pprint]
            [phase1.math.category :as cat]))

;; A tiny stepper/placeholder for the guided tour.
;; The layout will grow into a sequence of cards with prompts, hints, and links
;; to explorables. For now, we surface a single card that references the
;; category primitives.

(defn lesson-card []
  [:section
   [:h2 "Warm-up: categories as render pipelines"]
   [:p "Objects = scenes/layers. Morphisms = render passes. Composition = chaining passes."]
   [:p "Try editing `src/phase1/math/category.cljs` to build your own mini category."]
   [:pre (with-out-str (pprint/pprint (cat/example-category)))]] )

(defn lesson-tour []
  [:div
   [lesson-card]
   [:p "More guided cards + explorables coming soon in this phase."]])
