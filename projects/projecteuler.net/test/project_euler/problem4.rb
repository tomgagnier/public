# A palindromic number reads the same both ways. The largest palindrome made
# from the product of two 2-digit numbers is 9009 = 91 * 99.
#
# Find the largest palindrome made from the product of two 3-digit numbers.

puts (100..999).map{ |n| (n..999).map { |m| n * m }}.flatten.select {|n| n.to_s.reverse == n.to_s}.sort.last
