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

end