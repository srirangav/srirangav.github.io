#!/usr/bin/perl -wT
# genindex.pl - generate an html index
# $Id: genindex.pl 1083 2009-08-20 14:31:48Z ranga $

use strict;
use Getopt::Std;
use Cwd 'abs_path';

#
# main
#

my %OPTS = ();

my $ROOTDIR = "";
my $TITLE = "";
my $CAPTION = "";
my %DIRS = ();
my $ENTRIES = "";
my $NAVIGATION = "";
my $GENINDEX = 0;
my $HEADERF = "";
my $BODYF = "";
my $FOOTERF = "";

# TODO: Add support of navigation

getopts("b:r:t:h:f:c:", \%OPTS);

# base directory for generating navigation information

$ROOTDIR = $OPTS{'r'};
unless (defined($ROOTDIR) && -d $ROOTDIR) {
    printUsage();
    exit(1);
}

# page header 

$HEADERF = $OPTS{'h'};
unless (defined($HEADERF) && $HEADERF ne "") {
    printUsage();
    exit(1);
}

# page body

$BODYF = $OPTS{'b'};
unless (defined($BODYF) && $BODYF ne "") {
    printUsage();
    exit(1);
}

# generate an index if the page body is "dir"

$GENINDEX = 1 if ($BODYF eq "dir");

# page footer

$FOOTERF = $OPTS{'f'};
unless (defined($FOOTERF) && $FOOTERF ne "") {
    printUsage();
    exit(1);
}

# page title 

$TITLE = $OPTS{'t'};
unless (defined($TITLE) && $TITLE ne "") {
    printUsage();
    exit(1);
}

# optional page caption

$CAPTION = $OPTS{'c'};
$CAPTION = "" unless defined($CAPTION);

$DIRS{'ABS_ROOT'} = abs_path($ROOTDIR);
$DIRS{'CWD'} = abs_path(Cwd::getcwd());
$DIRS{'REL_ROOT'} = getRelPathToRoot($DIRS{'ABS_ROOT'}, $DIRS{'CWD'});

# index generation was not requested

if ($GENINDEX != 1) {
    exit (1) if (printHeader($DIRS{'REL_ROOT'}, $HEADERF, $TITLE, $CAPTION) != 0 ||
                 printBody($DIRS{'REL_ROOT'}, $BODYF) != 0 ||   
                 printFooter($DIRS{'REL_ROOT'}, $FOOTERF, $NAVIGATION) != 0);
    exit (0);
}

# generate the index

$ENTRIES = getFiles($DIRS{'CWD'});

if (defined($ENTRIES) && 
    ref($ENTRIES) eq "HASH" && 
    int($ENTRIES->{'count'}) > 0) {

    delete($ENTRIES->{'count'});

    exit(1) if (printHeader($DIRS{'REL_ROOT'}, $HEADERF, $TITLE, $CAPTION) != 0);

    print "<dl>\n";

    my $ENTRY = "";
    my $FILE = "";
    my $NAME = "";

    foreach $ENTRY (sort(keys(%{$ENTRIES}))) {
        my $SEP = "";
        print "\t<dt>$ENTRY</dt>\n\t\t<dd>\n";

        if (scalar(@{$ENTRIES->{$ENTRY}}) > 0) {
            foreach $FILE (sort(@{$ENTRIES->{$ENTRY}})) {
                ($NAME = $FILE) =~ s/_/ /g;
                $NAME =~ s/\.(txt|htm|html|pdf)$//;
                $NAME =~ s/((^\w)|(\s\w))/\U$1/xg;
                $NAME =~ s/ V / v\. /;
#                $NAME =~ s/([\w\']+)/\u\L$1/g;
                print <<EOHREF;
\t\t\t $SEP <a href="$FILE">$NAME</a> 
EOHREF
                $SEP = '&nbsp;&#183;&nbsp;';
            }
        }

        print "\t\t</dd>\n";
    }

    print "</dl>\n";
    
    exit(1) if (printFooter($DIRS{'REL_ROOT'}, $FOOTERF, $NAVIGATION) != 0);
}

exit(0);

#
# subroutines
#

#
# getRelPathToRoot - returns the relative path to the root directory
#

sub getRelPathToRoot
{
    my ($i, $dir, $rootdir, $curdir);
    
    $rootdir = shift @_;
    return "" unless (defined($rootdir) && -d $rootdir);
    
    $curdir = shift @_;
    return "" unless (defined($curdir) && -d $curdir);
    
    $dir = $curdir;
    while ($dir ne $rootdir && $dir ne '/') {
        $dir = abs_path("$dir/..");
        $i++;
    }

    $dir = "";
    for($dir = ""; $i > 0; $i--) {
        $dir .= "../";
    }
    
    return $dir;
}

#
# getFiles - return a list of files for the specified directory
#

sub getFiles
{
    my %entries = ();
    my $ref = 0;
    my $slot = "";
    my $entry = "";
    my $count = 0;
    my $dir = shift @_;

    if (defined($dir) && -d $dir) {
        if (opendir(DH,$dir)) {
            $entry = readdir(DH);
            while ($entry) {
        
                # look for txt, html or pdf files 
        
                if ($entry =~ /^\./ || 
                    $entry eq "index.html" || 
                    $entry !~ /\.(txt|htm|html|pdf)$/ || 
                    ! -f "$dir/$entry") {
                    $entry = readdir(DH);
                    next;
                }

                # get the first character to figure out the slot

                if ($entry =~ /^(.)/) {
                    $slot = $1;
                    $slot = "0-9" if ($slot =~ /^[0-9]$/);
                    $slot =~ tr/a-z/A-Z/;
                } else {
                    $entry = readdir(DH);
                    next;
                }

                $ref = $entries{$slot};
                push(@{$ref}, $entry);

                $entries{$slot} = $ref;
                $count++;

                $entry = readdir(DH);
            }

            closedir(DH);
        }
    } else {
        if (defined($dir)) {
            printError ("getFiles: $dir not a directory!");
        } else {
            printError ("getFiles: dir not defined!");
        }
    }

    $entries{'count'} = $count;
    
    return \%entries;
}

#
# printUsage - prints the usage statement
#

sub printUsage
{
    my $pgm = $0;
    $pgm =~ s/^.*\///;  

    print "usage: $pgm -r [root dir] -t [title] -h [header file]";
    print "-c [caption] -b [body file] -f [footer file]\n";

    return 0;
}

#
# printHeader - prints the page header
#

sub printHeader
{
    my $rootDir = "";
    my $headerf = "";
    my $title = "";
    my $caption = "";
    my $line = "";
    
    # get the root directory
    
    $rootDir = shift @_;
    unless (defined($rootDir) && -d $rootDir) {
        printError ("printHeader: rootDir not defined!");
        return -1;
    }
    $rootDir =~ s/\/+/\//g;
    $rootDir =~ s/\/$//;
    
    # get the html header file
    
    $headerf = shift @_;
    unless (defined($headerf) && -r "$rootDir/$headerf") {
        if (defined($headerf)) {
            printError ("printHeader: cannot read $rootDir/$headerf!");
        } else {
            printError ("printHeader: headerf not defined!");
        }
        return -1;
    }
    
    $title = shift @_;
    unless (defined($title) && $title ne "") {
        printError("printHeader: title not defined!");
        return -1;
    }
    
    $caption = shift @_;
    $caption = "" unless (defined($caption));
    
    unless (open(HEADERFD, "$rootDir/$headerf")) {
        printError("printHeader: cannot open $rootDir/$headerf!");
        return -1;
    }

    while (!eof(HEADERFD)) {
        $line = <HEADERFD>;
        next if (!defined($line));
        chomp($line);
        
        $line =~ s/\#\#TITLE\#\#/$title/g;
        $line =~ s/\#\#ROOTDIR\#\#/$rootDir/g;
        $line =~ s/\#\#CAPTION\#\#/$caption/g;
        
        print "$line\n";
    }

    close (HEADERFD);

    return 0;
}

#
# sub printBody - prints the page body
#

sub printBody
{
    my $rawbodyf = "";
    my $bodyf = "";
    my $rootDir = "";
    my $line = "";

    # get the root directory
    
    $rootDir = shift @_;
    return -1 unless (defined($rootDir) && -d $rootDir);
    $rootDir =~ s/\/+/\//g;
    $rootDir =~ s/\/$//;
    
    # get the html body file
    
    $rawbodyf = shift @_;
    if (defined($rawbodyf)) {
        $bodyf = "$rootDir/" if ($rawbodyf !~ /^\.\//);
        $bodyf .= "$rawbodyf";
    }
    return -1 unless (-r "$bodyf");

    # print out the body
    
    return -1 unless (open(BODYFD, "$bodyf"));
    
    while (!eof(BODYFD)) {
        $line = <BODYFD>;
        next if (!defined($line));
        print $line;
    }

    close(BODYFD);
    
    return 0;
}

#
# sub printFooter - prints the page footer
#

sub printFooter
{
    my $footerf = "";
    my $navigation = "";
    my $rootDir = "";
    my $line = "";
    
    # get the root directory
    
    $rootDir = shift @_;
    return -1 unless (defined($rootDir) && -d $rootDir);
    $rootDir =~ s/\/+/\//g;
    $rootDir =~ s/\/$//;

    # get the html footer file
    
    $footerf = shift @_;
    return -1 unless (defined($footerf) && -r "$rootDir/$footerf");

    # print out the footer
    
    return -1 unless (open(FOOTERFD, "$rootDir/$footerf"));

    while (!eof(FOOTERFD)) {
        $line = <FOOTERFD>;
        next if (!defined($line));
        chomp($line);

        $line =~ s/\#\#ROOTDIR\#\#/$rootDir/g;
        $line =~ s/\#\#NAVIGATION\#\#/$navigation/g;

        print "$line\n";
    }

    close(FOOTERFD);
    
    return 0;
}

#
# printError - print and error message to STDERR
#

sub printError
{
    print STDERR "ERROR: @_\n";
    return 0;
}