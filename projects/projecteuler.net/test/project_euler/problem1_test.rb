require 'test/unit'
require 'euler'

# If we list all the natural numbers below 10 that are multiples of 3 or 5,
# we get 3, 5, 6 and 9. The sum of these multiples is 23.
#
# Find the sum of all the multiples of 3 or 5 below 1000.


class EulerTest < Test::Unit::TestCase

  include Euler

  def test_when_less_than_10
    range = 1...10

    assert_equal(18, range.sum_of_multiples_of(3))
    assert_equal(5, range.sum_of_multiples_of(5))
    assert_equal(0, range.sum_of_multiples_of(15))

    assert_equal(23, range.sum_of_multiples_of(3) + range.sum_of_multiples_of(5) - range.sum_of_multiples_of(3 * 5))
  end

  def test_when_less_than_1000
    range = 1...1000

    puts range.sum_of_multiples_of(3) + range.sum_of_multiples_of(5) - range.sum_of_multiples_of(3 * 5)
  end
end

