(ns phase1.core
  (:require [reagent.core :as r]
            [phase1.lessons.adjoint-tour :as tour]))

;; The browser entry point for Phase 1.
;; We keep it tiny: mount a reagent root and hand control to the lesson tour.
;; The goal is rapid iteration: tweak a lesson card or visualization and
;; immediately see the results.

(defn app-root []
  [:div
   [:h1 "Riehl Cosmos — Phase 1"]
   [:p "Adjunctions as living code. Use the side-by-side explorables to watch laws hold or break."]
   [tour/lesson-tour]])

(defn ^:dev/after-load start []
  (r/render [app-root] (.getElementById js/document "app")))

(defn ^:export main []
  (start))
