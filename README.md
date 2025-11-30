# Riehl Cosmos — Phase 1: Adjunctions in Motion

An interactive, code-first learning track on category theory and ∞-categories, starting with adjunctions and the theorem that **left adjoints preserve colimits**. The learner (Z) is a senior graphics/rendering engineer who will use **ClojureScript + visuals** to internalize the math through direct manipulation and Moore-Method style exercises.

## Tech stack
- **Language:** ClojureScript (shadow-cljs)
- **Visuals:** p5.js / d3.js for 2D, three.js for 3D
- **UI:** Reagent (React wrapper) for lightweight controls

## Running the app
1. Install dependencies:
   ```bash
   npm install
   ```
2. Start the browser build with live reloading:
   ```bash
   npx shadow-cljs watch app
   ```
   Then open `public/index.html` in a browser (a minimal host page will be added as the explorables mature).

## Running tests
Executable-math style tests will live under `test/`. Run them via the node test build:
```bash
npx shadow-cljs watch test       # for TDD with reloading
npx shadow-cljs compile test     # one-off run
npx shadow-cljs run-tests test   # execute once in Node
```

## Phase 1 scope (adjunctions + colimits)
- Categories, functors, adjunctions, colimits.
- The theorem "left adjoints preserve colimits" expressed as code and visuals.
- Concrete algebraic examples (tensor distributing over direct sums; free/forgetful functors).
- Graphics/VFX analogies and explorables that let Z tweak sliders and watch the laws hold or fail.

## Project layout (initial)
- `src/phase1/core.cljs` — entry point and wiring for explorables.
- `src/phase1/math/category.cljs` — finite categories as data, validation utilities.
- `src/phase1/math/adjoint_colimits.cljs` — adjunction/colimit examples and Moore-style exercises.
- `src/phase1/views/adjoint_colimit_2d.cljs` — p5/d3 explorable scaffold.
- `src/phase1/views/adjoint_colimit_3d.cljs` — three.js explorable scaffold.
- `src/phase1/lessons/adjoint_tour.cljs` — guided tour + prompts.
- `test/phase1/math_test.cljs` — executable math statements.

## Next steps
- Flesh out the 2D explorable (p5.js) for `F(A ⊕ B)` vs `F(A) ⊕ F(B)`.
- Implement colimit constructions and adjunction witnesses in `adjoint_colimits.cljs`.
- Add node-based tests demonstrating that the sample left adjoints preserve coproducts.
