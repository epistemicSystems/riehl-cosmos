(ns phase1.math.adjoint-colimits
  (:require [phase1.math.category :as cat]))

;; Skeleton for adjunction + colimit stories.
;; This file will host finite diagrams, toy adjunctions, and executable proofs
;; that left adjoints preserve colimits. The structure is here; the exercises
;; (TODOs) invite Z to complete the arguments.

;; ------------------------------------------------------------
;; Representing diagrams and cocones (to be refined)
;; ------------------------------------------------------------

(defn make-diagram
  "Minimal diagram representation.

  - `shape` is a small category describing the shape of the diagram.
  - `assignment` is a map with two keys:
      :on-objects   -> function mapping each object in `shape` to an object
                       in the target category.
      :on-morphisms -> function mapping each morphism in `shape` to a morphism
                       in the target category.

  This keeps the data small but explicit enough to drive tests or visuals."
  [shape assignment]
  {:shape shape
   :assignment assignment})

(defn cocone
  "Construct a cocone over a diagram.

  - `apex`: the vertex object of the cocone.
  - `legs`: a map object-in-diagram -> morphism into the apex.

  This structure is intentionally plain so we can attach additional
  properties (commutativity checks, universal witnesses) as needed."
  [apex legs]
  {:apex apex :legs legs})

;; ------------------------------------------------------------
;; Proto-colimits and exercises
;; ------------------------------------------------------------

(defn- tag-left [x]
  [:left x])

(defn- tag-right [x]
  [:right x])

(defn- identity-morphism
  "Identity morphism for a finite Set-like object: map each element to itself."
  [object]
  (into {} (map (fn [x] [x x]) object)))

(defn- compose-maps
  "Compose two function-as-maps g ∘ f. Domain of f must match keys of g's codomain."
  [g f]
  (into {} (map (fn [[k v]] [k (get g v)]) f)))

(defn binary-coproduct
  "Binary coproduct in a finite Set-like category.

  Objects are finite sets (Clojure sets); morphisms are total functions
  represented as maps from domain elements to codomain elements.

  Returns a map with:
  - :coprod      the disjoint union of `a` and `b` via tagging.
  - :injections  two morphisms a→coprod and b→coprod.
  - :mediator    (fn [f g]) -> unique arrow from coprod to any target given
                  legs f : a→t and g : b→t.
  - :universal?  predicate that checks the universal property for supplied legs.
  "
  [category a b]
  (let [coprod (into #{} (concat (map tag-left a) (map tag-right b)))
        inj-a (into {} (map (fn [x] [x (tag-left x)]) a))
        inj-b (into {} (map (fn [y] [y (tag-right y)]) b))
        mediator (fn [f g]
                   (into {}
                         (concat (map (fn [x] [(tag-left x) (get f x)]) a)
                                 (map (fn [y] [(tag-right y) (get g y)]) b))))
        universal? (fn [target f g h]
                     (let [mediating (mediator f g)
                           left (compose-maps h inj-a)
                           right (compose-maps h inj-b)]
                       (and (= left f)
                            (= right g)
                            (= mediating h))))]
    {:coprod coprod
     :injections [inj-a inj-b]
     :mediator mediator
     :universal? universal?}))

;; ------------------------------------------------------------
;; Adjoint functors (sketch)
;; ------------------------------------------------------------

(defrecord Functor [name on-objects on-morphisms])

(defn functor
  "Build a functor from functions on objects and morphisms.
  TODO: enforce preservation of identities/composition as tests, not runtime."
  [name on-objects on-morphisms]
  (->Functor name on-objects on-morphisms))

(defn tagging-functor
  "A concrete Set-endofunctor that tags every element with the functor name.

  Think of this as adding a render-pass identifier to each layer in a scene."
  [name]
  (functor name
           (fn [object]
             (into #{} (map (fn [x] [name x]) object)))
           (fn [morphism]
             (into {}
                   (map (fn [[k v]]
                          [[name k] [name v]])
                        morphism)))))

(defn left-adjoint-preserves-coproducts?
  "Given a functor F that is left adjoint to some G, verify on a finite Set-like
  example that it preserves binary coproducts.

  We check that F(a ⊕ b) is isomorphic to F(a) ⊕ F(b) by explicitly building
  the two candidate coproducts and supplying a pair of inverse morphisms.
  Returns a map with the constructed data and a boolean :preserves? field."
  [F category a b]
  (let [{:keys [coprod] :as ab-coprod} (binary-coproduct category a b)
        Fa ((:on-objects F) a)
        Fb ((:on-objects F) b)
        F-coprod ((:on-objects F) coprod)
        {:keys [coprod f-coprod-inj-left f-coprod-inj-right] :as Fs-coprod}
        (let [{:keys [coprod injections]} (binary-coproduct category Fa Fb)]
          {:coprod coprod
           :f-coprod-inj-left (first injections)
           :f-coprod-inj-right (second injections)})
        phi (into {}
                  (map (fn [element]
                         (let [[tag payload] element
                               [lr x] payload]
                           (when-not (= tag (:name F))
                             (throw (ex-info "Unexpected tag in functor output" {:element element})))
                           [element (case lr
                                      :left (tag-left [tag x])
                                      :right (tag-right [tag x])
                                      (throw (ex-info "Unexpected coproduct tag" {:lr lr :element element})))]))))
                       F-coprod))
        psi (into {}
                  (map (fn [element]
                         (let [[lr payload] element]
                           [element [(:name F) [lr payload]]]))
                       (:coprod Fs-coprod)))
        id-F-coprod (identity-morphism F-coprod)
        id-Fs-coprod (identity-morphism (:coprod Fs-coprod))
        iso? (and (= id-F-coprod (compose-maps psi phi))
                  (= id-Fs-coprod (compose-maps phi psi)))]
    {:ab-coprod ab-coprod
     :F-coprod F-coprod
     :Fs-coprod Fs-coprod
     :iso {:forward phi :backward psi}
     :preserves? iso?}))

;; ------------------------------------------------------------
;; Concrete examples to flesh out next
;; ------------------------------------------------------------

;; 1) Tensor distributes over direct sum (symbolic):
;;    Represent finite-dimensional vector spaces as their dimensions.
;;    Let ⊕ be addition of dimensions; ⊗ be multiplication.
;;    TODO: build a functor L(U) = U ⊗ - and show L preserves ⊕.

;; 2) Set-based free/forgetful adjunction:
;;    TODO: encode a tiny free-monoid functor F : Set -> Mon and show it
;;    preserves coproducts (disjoint union) because it's a left adjoint.

;; 3) Graphics/VFX analogies:
;;    TODO: model a blur operator that distributes over layer compositing ⊕.
;;    TODO: model a projection functor that preserves a merge of geometry sets.

;; These TODOs will turn into executable proofs + visuals in the next steps.
