#!/usr/bin/perl -- # -*- Perl -*-

use strict;
my %languages = ('ca' => 'Catalan',
		 'cs' => 'Czech',
		 'da' => 'Danish',
		 'de' => 'German',
		 'el' => 'Greek',
		 'en' => 'English',
		 'es' => 'Spanish',
		 'et' => 'Estonian',
		 'fi' => 'Finnish',
		 'fr' => 'French',
		 'hu' => 'Hungarian',
		 'id' => 'Indonesian',
		 'it' => 'Italian',
		 'ja' => 'Japanese',
		 'ko' => 'Korean',
		 'nl' => 'Dutch',
		 'no' => 'Norwegian',
		 'pl' => 'Polish',
		 'pt' => 'Portuguese',
		 'pt_br' => 'Portuguese (Brazil)',
		 'ro' => 'Romanian',
		 'ru' => 'Russian',
		 'sk' => 'Slovak',
		 'sl' => 'Slovenian',
		 'sv' => 'Swedish',
		 'zh_cn' => 'Chinese (Continental)',
		 'zh_tw' => 'Chinese (Traditional)');

print <<EOF3;
<!DOCTYPE book PUBLIC "-//OASIS//DTD DocBook XML V4.1.2//EN"
               "http://www.oasis-open.org/docbook/xml/4.1.2/docbookx.dtd">
<book lang="de">
<bookinfo>
<title>Book Title</title>
<abstract>
<para>
To test multilingual capability, this book claims to be in German.
Of course, I don't know a word of German so it's really mostly
English.
</para>
</abstract>
</bookinfo>
EOF3

foreach my $lang (sort keys %languages) {
    my $langid = $lang;
    $langid =~ s/_//g;

    print <<EOF;
<chapter lang='$lang' id='${langid}chap'>
<chapterinfo>
<author id='${langid}author'>
  <surname>Surname</surname>
  <firstname>Firstname</firstname>
</author>
</chapterinfo>
<title>$languages{$lang}</title>
<note>
<para>
This paragraph would be in $languages{$lang}, if I knew any.
<quote>Quote test.</quote>
</para>
</note>
<para>Author formatting test: <xref linkend='${langid}author'/>.</para>
<para>Cross reference tests:
EOF

    foreach my $otherlang (sort keys %languages) {
	my $otherlangid = $otherlang;
	$otherlangid =~ s/_//g;
	if ($otherlang ne $lang) {
	    print "See <xref linkend='${otherlangid}chap'/>.\n";
	}
    }

    print <<EOF2;
</para>
</chapter>

EOF2
}

print "</book>\n";
