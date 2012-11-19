#!/usr/bin/ruby
# coding: utf-8

require 'fileutils'

OLD = "_old"

# "_old"の親フォルダ作成
unless Dir.exist?(OLD)
	Dir.mkdir(OLD)
end

# "_old"を含むフォルダの配列
def scan_old
	return Dir.glob("./#{OLD}/#{OLD}*")
end

# "_old"の後ろの数のカウント
def get_count
	count = -1
	dirs = scan_old
	dirs.map{ |s|
		line = s.delete(OLD).delete("./")		# "_old"と"./"の切り離し
		if line =~ /\d+/		# 数字の判定
			i = line.to_i
			if i > count	# countより数が大きければ
				count = i
			end
		end
	}
	
	return count
end

# フォルダの名前作成
def get_dirname
	i = get_count + 1
	dirname = "./#{OLD}/#{OLD}#{i}";
	return dirname
end

# コピペ、args:コピーしたい"フォルダ", コピー先のフォルダ
def copy_paste_dir(src, dest)
	unless File.exist?(dest)
		Dir::mkdir(dest)
	end
	
	unless Dir.exist?(src)
		Dir::mkdir(src)
	end
	
	FileUtils.copy_entry(src, "#{dest}#{src}")
	puts "copy:#{OLD}/#{src}"
end

# コピペ、args:コピーしたい"ファイル", コピー先のフォルダ
def copy_paste_file(src, dest)
	unless File.exist?(dest)
		Dir::mkdir(dest) == 0
	end
	
	FileUtils.copy(src, dest)
	puts "copy:#{OLD}/#{src}"
end


#これ以降はそれぞれ好みで

folder = get_dirname
copy_paste_dir("./ayamitsu", folder)
copy_paste_file("./EntityLiving.java", folder)
