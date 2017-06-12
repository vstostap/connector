var React = require('react');
var ZingChart = require('zingchart-react').core;
var CustomArea = require('./charts').area;
var CustomMap = require('./charts').map;
var CustomStackedBar = require('./charts').stackedBar;
var DataStore = require('./datastore.js');
var socket = require('socket.io-client')();
zingchart.loadModules('maps,maps-world-countries');
socket.connect('http://0.0.0.0');
var websocket = new WebSocket('ws://0.0.0.0:9000/twitter');
var websocketStop = new WebSocket('ws:0.0.0.0:9000/stop');

/*
data: {
 user: 234,
 page: [
    { count: 37, title: '3Dprinting' }, ...
 ],
 location: {
    { count: 19, title: 'ind' }, ...
}
*/

var initial = {
    originalState: DataStore.generateFakeDataset(DataStore.resolution.hour),
    mapSeries : {},
    area: [],
    distribution: []
};


var App = React.createClass({
    getInitialState : function(){
        return Object.assign(initial, {filters: null});
    },

    startProcess: function () {
        var filters = this.state.filters;
        console.log(filters);
        websocket.send(filters);
        this.setState(initial);
        this.setState({filters: null});
    },

    stopProcess: function () {
        websocketStop.send('stop');
        this.setState(initial);
        this.setState({filters: null});
    },

    handleFilters: function (e) {
        this.setState({filters: e.target.value});
    },

    render : function(){
        var colors = ["#076d7f", "#029fbc", "#00BFFF", "#00D9FF", "#bae2ff"];
        var distributionSeries = this.state.distribution;
        distributionSeries.forEach(function (serie, index) {
                if (colors[index]) serie.backgroundColor = colors[index]
            }
        );
        return (
                <div className="base">
                    <div className="form-inline">
                        <div className="form-group col-xs-12">
                            <label htmlFor="filters"> Keywords </label>
                            <input className="form-control" id="filters" value={this.state.filters} onChange={this.handleFilters}  placeholder="Type your keywords here..." type="text" />
                            <button type="button" className="btn btn-primary" onClick={this.startProcess}> Start </button>
                            <button type="button" className="btn btn-danger" onClick={this.stopProcess}>Stop</button>
                        </div>
                    </div>
                    <div className="charts">
                        <section>
                            <div id="chart-containers">
                                {this.state.area.length > 1 ? <CustomArea id="chart1" height="300" width="1000" values={this.state.area}/> : null}
                                {this.state.distribution.length ? <CustomStackedBar id="chart2" height="300" width="1000" series={distributionSeries}/> : null}
                                <CustomMap id="chart3" height="300" width="1000" series={this.state.mapSeries}/>
                            </div>
                        </section>
                    </div>
                </div>
        )
    },
    componentDidMount() {
        socket.on('overall', this._setOverall);
        socket.on('distribution', this._setDistribution);
        setInterval(this.modifyData, 2000);
    },

    _setOverall: function (data) {
        console.log(data);
        console.log('///////////');
        var area = this.state.area;
        area.push(parseInt(data.area));
        this.setState({area: area})
    },

    _setDistribution: function (data) {
        console.log(data);
        console.log('/////');
        var scope = this;
        var newData = DataStore.generateSingleDataset(this.state.originalState[this.state.originalState.length-1], 3600000);
        var distribution = this.state.distribution;
        var found = false;
        distribution.forEach(function (el) {
            if(el.text === data.title) {
                found = true;
                (el.values).push(parseInt(data.count));
            }
        });
        if(!found) {
            distribution.push({
                text: data.title,
                values: [parseInt(data.count)]
            });
        }
        this.setState({
            distribution: distribution,
            mapSeries: scope.generateMapValues(newData.location)
        })
    },

    generateMapValues: function(aLocations) {
        var _mapValues = {};
        var colors = ["#ededed", "#afffc8", "#73f05f", "#4aa320"];
        var sorted = aLocations.sort(function (a, b) {
            return a.count - b.count;
        });
        var i = 0;
        var arr = split(sorted, 4);

        function split(a, n) {
            var len = a.length, out = [], i = 0;
            while (i < len) {
                var size = Math.ceil((len - i) / n--);
                out.push(a.slice(i, i + size));
                i += size;
            }
            return out;
        }

        for (var i = 0; i < arr.length; i++) {
            for (var k = 0; k < arr[i].length; k++) {
                _mapValues[arr[i][k].title.toUpperCase()] = {
                    backgroundColor: colors[i]
                }
            }
        }
        return _mapValues;
    }
});

React.render(<App />, document.getElementById('root'));