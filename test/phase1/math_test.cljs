(ns phase1.math-test
  (:require [cljs.test :refer-macros [deftest is testing]]
            [phase1.math.category :as cat]))

(deftest example-category-valid
  (testing "tiny render pipeline category satisfies axioms"
    (is (true? (cat/valid-category? cat/example-category)))))

(deftest identity-composition-behavior
  (testing "identities behave under composition"
    (let [c cat/example-category
          f (first (filter #(= :f (:name %)) (cat/morphisms c)))]
      (is (= f (cat/compose c f (cat/id-of c :A))))
      (is (= f (cat/compose c (cat/id-of c :B) f))))))
