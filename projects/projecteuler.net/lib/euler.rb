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

  def fibonacci
    Enumerator.new do |fibonacci_series|
      n0 = n1 = 1
      loop do
        fibonacci_series << n0
        n0, n1 = n1, n0 + n1
      end
    end
  end

  def divisors_of(n)
    #divisors = []
    #(1..n).each do |divisor|
    #  divisors << divisor if n % divisor == 0
    #end
    #divisors
    factors_of(n).combinations
  end

  def factors_of(n)
    factors = []
    factor = 2
    while factor <= n
      if n % factor == 0
        factors << factor
        n /= factor
      else
        factor = factor + 1
      end
    end
    factors
  end

  def binomial_coefficient(row, column)
    if column == 0 || column == row
      1
    else
      binomial_coefficient(row - 1, column - 1) + binomial_coefficient(row - 1, column)
    end
  end


end