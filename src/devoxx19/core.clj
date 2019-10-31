(ns devoxx19.core)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; literals and atomic data types ;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; integers
42

;; doubles
4.2

;; booleans
true
false

;; strings
"hello"

;; symbols
'hello

;; keywords
:hello

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; data structures ;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; lists
;; linked lists
'(1 2 3)

;; grow at start
(conj '(1 2 3) 4)

;; vectors
;; random access (log32)
[1 2 3]

;; grow at end
(conj [1 2 3] 4)

;; sets
#{1 2 3}

;; maps
{:a 1 :b 2 :c 3}

;; data structures can nest:
['(1 2 3) #{"hello"}]

;; Immutability

(def my-list '(1 2 3))
(conj my-list 4)
my-list

(def my-map {:a 1 :b 2 :c 3})
(assoc my-map :d 4)

;; simple to reason about our code
;; eliminates a class of concurrency problems

;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; functions ;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;

;; Clojure is a lisp, and uses prefix notation

(fn [x y z] (* (+ x y) 5))

;; how would we invoke it?

((fn [x y] (* (+ x y) 5)) 0 1)

(defn foo [x y]
  (* (+ x y) 5))

(foo 0 1)

;;;;;;;;;;;;;;;;;;;;;;
;;; pure functions ;;;
;;;;;;;;;;;;;;;;;;;;;;

;; pure functions are functions which always return the same result given the same arguments
;; no side-effect: files / devices / network / randomness / time
;; no dependence on state

;; why care?
;; simple to reason about
;; simple to test
;; parallelizable
;; composable

;; what else can we do with functions?

;; functions can be returned from other functions
(defn foo [x]
  (fn [y]
    (* (+ x y) 5)))

(foo 0)
((foo 0) 1)

;; functions can accept other functions as argument
(def my-odd? (complement even?))

(my-odd? 0)
(my-odd? 1)

;; higher order functions:

(map inc [1 2 3])
(filter even? (map inc [1 2 3]))

(->> [1 2 3]
     (map inc)
     (filter even?)
     (map (fn [x] (* x 3))))

;; other things that are functions:
({:a 1 :b 2} :a)
(:a {:a 1 :b 2})

;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; sequences ;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;

;; Sequences is one of the most important Clojure abstractions.
;; In brief, sequences represent logical lists that are traveresed sequentially.
;; The abstraction provides several key functions by which we use sequences.

;; `first` -- returns the first element of the seq, or `nil` if none exists.
;; `rest` -- returns a seq of the remainder of the given seq. Might return an empty seq
;;           but never `nil`.
;; `next` -- like `rest`, but instead of returning an empty seq will return `nil`.
;;           `next` is "less lazy" than `rest`.
;; `cons` -- add an element to the beginning of a seq.

(first [1 2 3])
(rest [1 2 3])
(cons 4 [1 2 3])

(rest {:a 1 :b 2 :c 3})

;; The collections we've seen thus far are `seqable`, meaning they can be turned to a sequence

(seq [1 2 3 4 5])
(seq {:a 1 :b 2 :c 3})

;; Not only collections are sequences. Other less obvious things are `seqable` and can be turned into a sequence.

;; Strings:

(seqable? "aaaa")
(seq "abcde")
(map #(= \a %) "abdceabca")

;; Java arrays:

(seqable? (make-array String 10))
(filter even? (into-array [1 2 3 4]))

;; java.util.* Collections and classes that implements java.util.Iterator

(seqable? (java.util.ArrayList.))

;; Important: while Java arrays/collections can be used as seqs, they do not
;; provide the immutability guarantees of Clojure collections. Clojure does not copy
;; their contents when turning them into seqs, so be careful!

;;;;;;;;;;;;;;;;;;; Lazy sequences ;;;;;;;;;;;;;;;;;;;


;; Sequences don't have to be backed up by an underlying collection.
;; We said seqs are "logical lists", whose abstraction is defined by
;; the functions `first` and `rest`. These functions allow us to create
;; lazy sequences that are only materialized as they are consumed - hence the why
;; we call them "lazy".
;; They can even be infinite.

;; For example, the `range` function creates a range of some size.
;; When we call (range 1000), none of the elements of the sequences are materialized.
;; It only happens as we consume the sequence.

(def my-range (range 1000000000))

;; `my-range` above is a sequence, but none of the elements are there yet.
;; However, if we were to call `take` for example, it would start to be materialized.

(take 5 my-range)

;; We materialized 5 numbers. The rest of the elements remain unmaterialized, until we need them.


;; Most sequence functions like `map` and `filter` return a lazy sequence. This means
;; that no work is done until we begin consuming them.


;; It is important to realize that lazy seqs might not be fully lazy.
;; Some lazy sequences are "chunked", meaning that they are consumed a chunk at the time.

;; For example, `map` returns a lazy sequence that is chunked at 32 elements:

(first (map (fn [x] (println x) (* x 2)) (range 500)))

;;;;;;;;;;;;;;;; Why is laziness important in functional programming ;;;;;;;;;;;;;;;;

;; How many iterations?

(doall (filter even? (map inc (range 10))))


;; exercise

(defn collatz
  "Returns a lazy sequence of `n`'s Collatz sequence.
  Hint: use `iterate`"
  [n])

(defn collatz-satisfiable?
  "Returns true if the Collatz conjecture holds for `n`.
  Hint: use `some`"
  [n])

(defn colltaz-prefix
  "Returns a sequence made up of all the numbers in `n`'s Collatz sequence until the first 1.
  Hint: use `take-while`"
  [n])

(defn max' [s] (apply max s))

(defn longest-collatz
  "Returns the number between 1 and `n` that has the longest Collates prefix.
  Hint: `map` and `range` "
  [n])


;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;; Java Interop ;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;

(def sb (StringBuilder.))
(.append sb "hello")
(.append sb "world")
(.toString sb)

;; static methods can also be called:
(Integer/parseInt "123")

;;;;;;;;;;;;;;;;;;
;;;;;; data ;;;;;;
;;;;;;;;;;;;;;;;;;
nnnn
;; ways to model data:

;; records
(defrecord Color [r g b])
(->Color 255 255 255)

;; but most often we will use Clojure data structures
{:r 255 :g 255 :b 255}

;; Modeling via data allows us to use the same set of abstractions provided by Clojure
;; when working with different domains
(def people [{:name "Joe" :age 42} {:name "Foo" :age 33}])

(->> people
     (sort-by :age)
     (map (fn [person]
            (update person :name clojure.string/upper-case))))

;; protocols give us type dispatch
(defprotocol Countable
  (countit [x]))

(extend-type String
  Countable
  (countit [s] (.length s)))

(extend-type Color
  Countable
  (countit [c] (+ (:r c) (:g c) (:b c))))

(countit "Hello, Devoxx")
(countit (->Color 255 255 255))

;;;;;;;;;;;;;;;;;;;
;;;;;; State ;;;;;;
;;;;;;;;;;;;;;;;;;;

(def a (atom {}))
(swap! a (fn [m] (assoc m :a 1)))
@a


