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

end