PrimeFaces.widget.Poll=PrimeFaces.widget.BaseWidget.extend({init:function(a){this.cfg=a;this.id=this.cfg.id;this.active=false;if(this.cfg.autoStart){this.start()}},refresh:function(a){if(this.isActive()){this.stop()}this.init(a)},start:function(){this.timer=setInterval(this.cfg.fn,(this.cfg.frequency*1000));this.active=true},stop:function(){clearInterval(this.timer);this.active=false},handleComplete:function(c,a,b){if(b.stop){this.stop()}},isActive:function(){return this.active}});