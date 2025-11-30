(ns phase1.lessons.adjoint-tour
  (:require [cljs.pprint :as pprint]
            [phase1.math.category :as cat]
            [phase1.math.adjoint-colimits :as adj]))

;; A tiny stepper/placeholder for the guided tour.
;; The layout will grow into a sequence of cards with prompts, hints, and links
;; to explorables. For now, we surface a single card that references the
;; category primitives.

(defn lesson-card []
  (let [a #{:mesh :light}
        b #{:volume}
        {:keys [coprod injections mediator]} (adj/binary-coproduct nil a b)
        blur-f (adj/tagging-functor :blur)
        preserves (adj/left-adjoint-preserves-coproducts? blur-f nil a b)]
    [:section
     [:h2 "Warm-up: categories as render pipelines"]
     [:p "Objects = scenes/layers. Morphisms = render passes. Composition = chaining passes."]
     [:p "Try editing `src/phase1/math/category.cljs` to build your own mini category."]
     [:pre (with-out-str (pprint/pprint (cat/example-category)))]
     [:h3 "Binary coproduct as tagged union"]
     [:p "We treat coproducts like disjoint merges of layers. The injections tag each input before union."]
     [:pre (with-out-str (pprint/pprint {:coprod coprod :injections injections}))]
     [:p "Given legs f : a → t and g : b → t, the mediator stitches them into one arrow from the coproduct."]
     [:pre (with-out-str (pprint/pprint (mediator {:mesh :frame-A :light :frame-L}
                                                 {:volume :frame-V})))]
     [:h3 "Left adjoint vibe check"]
     [:p "A tagging functor (think blur pass) distributes over the coproduct; the forward/backward maps form an isomorphism."]
     [:pre (with-out-str (pprint/pprint (:iso preserves)))]
     [:p (if (:preserves? preserves)
           "F(a ⊕ b) ≅ F(a) ⊕ F(b) passes the check."
           "Something's off—the isomorphism failed.")]]))

(defn lesson-tour []
  [:div
   [lesson-card]
   [:p "More guided cards + explorables coming soon in this phase."]])
