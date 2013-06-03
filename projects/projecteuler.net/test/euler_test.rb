require 'test/unit'
require 'euler'

class EulerTest < Test::Unit::TestCase

  include Euler

  def test_sum
    assert_equal(3, (1..2).sum)
    assert_equal(3, (1...3).sum)

    assert_equal(55, (1..10).sum)
    assert_equal(55, (1...11).sum)

    assert_equal(54, (2..10).sum)
  end

  def test_sum_of_multiples_of
    assert_equal(0, (1...3).sum_of_multiples_of(3))
    assert_equal(3, (1..3).sum_of_multiples_of(3))
    assert_equal(9, (1..6).sum_of_multiples_of(3))
    assert_equal(18, (1..9).sum_of_multiples_of(3))

    assert_equal(5, (1..9).sum_of_multiples_of(5))

    assert_equal(0, (1..9).sum_of_multiples_of(15))
  end

  def test_binomial_coefficient
    assert_equal(1, binomial_coefficient(0, 0))

    assert_equal(1, binomial_coefficient(1, 0))
    assert_equal(1, binomial_coefficient(1, 1))

    assert_equal(1, binomial_coefficient(2, 0))
    assert_equal(2, binomial_coefficient(2, 1))
    assert_equal(1, binomial_coefficient(2, 2))

    assert_equal(1, binomial_coefficient(3, 0))
    assert_equal(3, binomial_coefficient(3, 1))
    assert_equal(3, binomial_coefficient(3, 2))
    assert_equal(1, binomial_coefficient(3, 3))

    assert_equal(1, binomial_coefficient(4, 0))
    assert_equal(4, binomial_coefficient(4, 1))
    assert_equal(6, binomial_coefficient(4, 2))
    assert_equal(4, binomial_coefficient(4, 3))
    assert_equal(1, binomial_coefficient(4, 4))
  end

  def test_fibonacci
    assert_equal([1, 1, 2, 3, 5, 8, 13], fibonacci.take(7))
    assert_equal([1, 1, 2, 3, 5, 8, 13], fibonacci.take_while { |n| n < 20 })

    enumerator = fibonacci
    assert_equal(1, enumerator.next)
    assert_equal(1, enumerator.next)
    assert_equal(2, enumerator.next)
    assert_equal(3, enumerator.next)
    assert_equal(5, enumerator.next)
    assert_equal(8, enumerator.next)
    assert_equal(13, enumerator.next)
    assert_equal(21, enumerator.next)
  end

  def test_factors_of
    assert_equal([], factors_of(1))
    assert_equal([2], factors_of(2))
    assert_equal([3], factors_of(3))
    assert_equal([2, 2], factors_of(4))
    assert_equal([5], factors_of(5))
    assert_equal([2, 3], factors_of(6))
    assert_equal([5, 7, 13, 29], factors_of(13195))
  end

  def test_divisors_of
    assert_equal([1, 2, 3, 4, 6, 8, 12, 24], divisors_of(24))
  end

end