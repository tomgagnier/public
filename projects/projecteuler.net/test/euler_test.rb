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

end