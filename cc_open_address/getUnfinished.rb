#!/usr/bin/ruby
require 'set'
wholeListFile = File.open("wholeList", "r")
wholeList = Set.new 
wholeListFile.each_line do |line|
  wholeList.add(line.chop)
end



puts "Total lines: #{wholeList.size}"
finishedFile = File.open("finished", "r")
finishedFile.each_line do |line|
  fileStr = line.split(" ")[0]
  #puts fileStr
  wholeList.delete(fileStr)
  #if wholeList.member?(fileStr)
    #puts fileStr
  #end
end
puts "Left lines: #{wholeList.size}"

File.open("unfinished", "w") do |unfinishedFile|
  wholeList.each do |str|
    unfinishedFile.write(str + "\n")
  end
end

wholeListFile.close
finishedFile .close
