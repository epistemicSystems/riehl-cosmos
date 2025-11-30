(ns phase1.math.category)

;; ------------------------------------------------------------
;; Finite categories as data: objects, morphisms, identities, composition.
;;
;; Why S-expressions? The learner (Z) is a graphics engineer; code-as-data
;; mirrors node graphs and shader pipelines. Here, a category is a compact map
;; you can inspect, pretty-print, and mutate while running the visuals.
;;
;; (Council of Luminaries)
;; Grothendieck: "We seek the *shape* of reasoning, not the coordinates."
;; Noether: "Laws live in the invariants of composition and identity."
;; McLarty: "Even a tiny finite category hints at the grand narrative."
;; Tao: "Think of morphisms as operators in a render stack; associativity is
;;       the promise that regrouping passes won't change the final frame."
;; ------------------------------------------------------------

(defn make-morphism
  "Create a morphism record. `name` is an id (keyword/string), dom/cod are objects."
  [name dom cod]
  {:name name :dom dom :cod cod})

(defn make-category
  "Build a finite category.

  - `objects`: a set of objects (keywords/symbols).
  - `morphisms`: a vector/list of morphism maps (see `make-morphism`).
  - `identities`: map object -> morphism name designating its identity.
  - `composition`: map [g-name f-name] -> h-name meaning g ∘ f = h.

  Composition is partial: missing entries mean the pair is not composable or
  not defined in this toy universe.
  "
  [{:keys [objects morphisms identities composition]}]
  {:objects objects
   :morphisms morphisms
   :identities identities
   :composition composition})

(defn objects [category]
  (:objects category))

(defn morphism-map [category]
  (into {} (map (juxt :name identity) (:morphisms category))))

(defn morphisms [category]
  (:morphisms category))

(defn id-of [category object]
  (get (morphism-map category) (get (:identities category) object)))

(defn compose
  "Return the composite morphism map g ∘ f if defined, else nil.
  Use morphism names to look up the result from the composition table."
  [category g f]
  (let [table (:composition category)
        lookup (morphism-map category)
        name* (get table [(:name g) (:name f)])]
    (get lookup name*)))

(defn- composable? [f g]
  (= (:cod f) (:dom g)))

(defn valid-category?
  "Check associativity and identity axioms on a finite category.
  This is deliberately explicit—good for executable math and readable failure messages."
  [category]
  (let [objs (objects category)
        morphs (morphisms category)
        id-map (:identities category)
        lookup (morphism-map category)
        all-ids (map (partial id-of category) objs)
        ;; helper predicates
        morph-valid? (fn [{:keys [dom cod]}]
                       (and (contains? objs dom)
                            (contains? objs cod)))
        identity-law? (fn [f]
                        (let [id-dom (id-of category (:dom f))
                              id-cod (id-of category (:cod f))
                              left (compose category f id-dom)
                              right (compose category id-cod f)]
                          (and (= f left) (= f right))))
        associativity-law? (fn []
                             (every?
                              true?
                              (for [f morphs
                                    g morphs
                                    h morphs
                                    :when (and (composable? f g)
                                               (composable? g h)
                                               (compose category g f)
                                               (compose category h g))]
                                (let [left (compose category (compose category h g) f)
                                      right (compose category h (compose category g f))]
                                  (= left right)))))])
    (and
     ;; all morphisms have dom/cod in the object set
     (every? morph-valid? morphs)
     ;; identities are present and well-typed
     (every? some? all-ids)
     (every? #(= (:dom %) (:cod %)) all-ids)
     ;; identity laws
     (every? identity-law? morphs)
     ;; associativity for all composable triples
     (associativity-law?))))

;; ------------------------------------------------------------
;; Tiny concrete category: two objects A → B
;; Think of A = "raw geometry", B = "rendered frame".
;; A single non-identity arrow `f` is a render pass from geometry to frame.
;; ------------------------------------------------------------

(def example-category
  (let [objects #{:A :B}
        morphisms [(make-morphism :id-A :A :A)
                   (make-morphism :id-B :B :B)
                   (make-morphism :f :A :B)]
        identities {:A :id-A :B :id-B}
        composition {[:id-A :id-A] :id-A
                     [:id-B :id-B] :id-B
                     [:id-B :f] :f
                     [:f :id-A] :f}]
    (make-category {:objects objects
                    :morphisms morphisms
                    :identities identities
                    :composition composition})))

(comment
  ;; Quick smoke-check: does our tiny category satisfy the laws?
  (valid-category? example-category)

  ;; Prompt for Z (Moore-Method flavored):
  ;; - Add another morphism `g : B -> B` meaning "post-process blur".
  ;; - Update the composition table so `g ∘ f` is defined.
  ;; - Re-run `valid-category?` and see how associativity constrains your choices.
  )
