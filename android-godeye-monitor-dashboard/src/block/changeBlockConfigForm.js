import React, {Component} from 'react';
import '../App.css';
import {Button, InputNumber, Form} from 'antd'

class ChangeBlockConfigForm extends Component {
    handleSubmit = e => {
        e.preventDefault();
        this.props.form.validateFields((err, fieldsValue) => {
            if (!err) {
                this.props.form.resetFields();
                this.props.handleChange(fieldsValue.BlockTime);
            }
        });
    };

    render() {
        const {getFieldDecorator} = this.props.form;
        return (
            <Form layout="inline" onSubmit={this.handleSubmit}>
                <Form.Item>
                    {getFieldDecorator('BlockTime', {
                        rules: [{required: true}],
                    })(
                        <InputNumber min={1} max={10000}/>
                    )}
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit">
                        OK
                    </Button>
                </Form.Item>
            </Form>
        );
    }
}

const ChangeBlockConfigFormInstance = Form.create({name: 'ChangeBlockConfigForm'})(ChangeBlockConfigForm);

export default ChangeBlockConfigFormInstance