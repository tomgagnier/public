module Euler

  class ::Range
    def sum
      def summation(n)
        n * (n + 1) / 2
      end

      summation(last_number()) - summation(first - 1)
    end

    def sum_of_multiples_of(multiple)
      multiple * Range.new(first(), last_number() / multiple).sum
    end

    private

    def last_number
      last - (exclude_end? ? 1 : 0)
    end
  end

  def binomial_coefficient(row, column)
    if column == 0 || column == row
      1
    else
      binomial_coefficient(row - 1, column - 1) + binomial_coefficient(row - 1, column)
    end
  end

end