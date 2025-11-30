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
  "Placeholder: a diagram is a map of shape (a small category) to objects/morphisms
  in a target category. Represent it minimally so we can experiment.
  TODO: design a lightweight diagram type with arity-specific helpers."
  [shape assignment]
  {:shape shape
   :assignment assignment})

(defn cocone
  "Construct a cocone over a diagram.
  TODO: encode the universal property for binary coproducts (A ⊕ B)."
  [apex legs]
  {:apex apex :legs legs})

;; ------------------------------------------------------------
;; Proto-colimits and exercises
;; ------------------------------------------------------------

(defn binary-coproduct
  "Moore-Method TODO: implement a binary coproduct for a tiny category where
  objects are keywords and morphisms are basic maps.
  - Input: two objects a b in a category C
  - Output: {:coprod c :injections [i1 i2]} such that for any other target t
    and arrows f : a -> t, g : b -> t, there is a unique mediating arrow.
  Hint: start with the Set-like category where objects are small sets and
  morphisms are functions represented as Clojure maps.
  "
  [category a b]
  (throw (ex-info "TODO: implement binary-coproduct" {:a a :b b})))

;; ------------------------------------------------------------
;; Adjoint functors (sketch)
;; ------------------------------------------------------------

(defrecord Functor [name on-objects on-morphisms])

(defn functor
  "Build a functor from functions on objects and morphisms.
  TODO: enforce preservation of identities/composition as tests, not runtime."
  [name on-objects on-morphisms]
  (->Functor name on-objects on-morphisms))

(defn left-adjoint-preserves-coproducts?
  "Given a functor F that is left adjoint to some G, verify on a finite example
  that it preserves binary coproducts.
  TODO: implement using `binary-coproduct` once filled in.
  "
  [F category a b]
  (throw (ex-info "TODO: show that F preserves coproducts" {:functor (:name F)})))

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
