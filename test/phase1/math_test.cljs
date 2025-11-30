(ns phase1.math-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [phase1.math.category :as cat]
            [phase1.math.adjoint-colimits :as adj]))

(deftest example-category-valid
  (testing "tiny render pipeline category satisfies axioms"
    (is (true? (cat/valid-category? cat/example-category)))))

(deftest identity-composition-behavior
  (testing "identities behave under composition"
    (let [c cat/example-category
          f (first (filter #(= :f (:name %)) (cat/morphisms c)))]
      (is (= f (cat/compose c f (cat/id-of c :A))))
      (is (= f (cat/compose c (cat/id-of c :B) f))))))

(deftest binary-coproduct-behavior
  (testing "tagged union coproduct builds injections and mediators"
    (let [a #{:mesh :light}
          b #{:volume}
          {:keys [coprod injections mediator universal?]} (adj/binary-coproduct nil a b)
          [i1 i2] injections
          target #{:frame-A :frame-L :frame-V}
          f {:mesh :frame-A :light :frame-L}
          g {:volume :frame-V}
          h (mediator f g)]
      (is (= coprod #{[:left :mesh] [:left :light] [:right :volume]}))
      (is (= i1 {:mesh [:left :mesh] :light [:left :light]}))
      (is (= i2 {:volume [:right :volume]}))
      (is (= h {[:left :mesh] :frame-A
                [:left :light] :frame-L
                [:right :volume] :frame-V}))
      (is (true? (universal? target f g h))))))

(deftest left-adjoint-preserves-coproducts-example
  (testing "tagging functor distributes over coproduct"
    (let [F (adj/tagging-functor :blur)
          a #{:mesh :light}
          b #{:volume}
          result (adj/left-adjoint-preserves-coproducts? F nil a b)]
      (is (true? (:preserves? result))))))
