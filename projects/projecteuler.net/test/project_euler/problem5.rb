require 'euler'
include Euler
# 2520 is the smallest number that can be divided by each of the numbers from 1 to 10 without any remainder.
#
# What is the smallest positive number that is evenly divisible by all of the numbers from 1 to 20?

#def factors_of_least_common_multiple(factored_number_list)
#  factored_number_list.reduce(Hash.new(0)) do |result, factored_number|
#    factored_number.each { |f, c| result[f] = [result[f], c].max }; result
#  end
#end
#
#def

#puts factors_of_least_common_multiple((1..20).map { |n| factors_of(n) })

factors = (1..20).map { |n| factors_of(n) }

puts "#{factors}"

x = factors.reduce([]) {|x, y| puts "#{y-x}"; x + y}

puts "#{x}"